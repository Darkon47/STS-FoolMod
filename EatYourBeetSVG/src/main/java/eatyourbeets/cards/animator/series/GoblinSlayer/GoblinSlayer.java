package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GoblinSlayer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoblinSlayer.class).SetAttack(1, CardRarity.RARE);

    public GoblinSlayer()
    {
        super(DATA);

        Initialize(4, 4);
        SetUpgrade(3, 3);
        SetScaling(1, 0, 1);

        this.cropPortrait = false;
        SetRetain(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int turnCount = PlayerStatistics.getTurnCount();
        if (turnCount % 2 == 1)
        {
            int goblins = 1;
            if (turnCount > 3)
            {
                goblins += 1;
            }
            if (turnCount > 7)
            {
                goblins += 1;
            }

            GameActions.Bottom.Add(new CreateRandomGoblins(goblins));
        }
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (player.exhaustPile.size() * 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        MoveCards(p.discardPile, p.exhaustPile);
        MoveCards(p.hand, p.exhaustPile);

        GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Top.GainBlock(block);
    }

    private void MoveCards(CardGroup source, CardGroup destination)
    {
        float duration = 0.3f;

        for (AbstractCard card : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(card))
            {
                GameActions.Top.MoveCard(card, source, destination)
                        .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                        .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
            }
        }
    }
}