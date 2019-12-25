package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.PlayCardFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.DrawPileCardPreview;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import patches.AbstractEnums;

public class Illya extends AnimatorCard
{
    public static final String ID = Register(Illya.class.getSimpleName(), EYBCardBadge.Exhaust);

    private final DrawPileCardPreview drawPileCardPreview = new DrawPileCardPreview(Illya::FindBestCard);

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, -2);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        drawPileCardPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        drawPileCardPreview.Update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        drawPileCardPreview.Render(sb);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(__ ->
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (!DrawBerserker(p.drawPile))
            {
                if (!DrawBerserker(p.discardPile))
                {
                    if (!DrawBerserker(p.exhaustPile))
                    {
                        DrawBerserker(p.hand);
                    }
                }
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard card = FindBestCard(m);
        if (card != null)
        {
            GameActions.Top.Add(new PlayCardFromPile(card, p.drawPile, false, false, m));
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
        }
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, group)
                    .ShowEffect(true, true);
                }

                GameActions.Bottom.GainEnergy(1);

                return true;
            }
        }

        return false;
    }

    private static AbstractCard FindBestCard(AbstractMonster target)
    {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.drawPile.group)
        {
            if (c.type == CardType.ATTACK && c.cardPlayable(target) && !c.tags.contains(AbstractEnums.CardTags.TEMPORARY))
            {
                c.calculateCardDamage(target);
                if (c.damage > maxDamage)
                {
                    maxDamage = c.damage;
                    bestCard = c;
                }
                c.resetAttributes();
            }
        }

        return bestCard;
    }
}