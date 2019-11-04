package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.actions.common.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;

public class Illya extends AnimatorCard
{
    public static final String ID = Register(Illya.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 6, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

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
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameActionsHelper.AddToBottom(new MoveSpecificCardAction(c, AbstractDungeon.player.hand, group, true));
                }

                GameActionsHelper.AddToBottom(new ModifyDamageAction(c.uuid, secondaryValue));

                return true;
            }
        }

        return false;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.type == CardType.ATTACK && c.cardPlayable(m))
            {
                c.calculateCardDamage(m);
                if (c.damage > maxDamage)
                {
                    maxDamage = c.damage;
                    bestCard = c;
                }
            }
        }

        if (bestCard != null)
        {
            GameActionsHelper.AddToTop(new PlayCardFromPileAction(bestCard, p.drawPile, false, false, m));
        }

        GameActionsHelper.ApplyPower(p, p, new SelfDamagePower(p, magicNumber), magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-2);
        }
    }
}