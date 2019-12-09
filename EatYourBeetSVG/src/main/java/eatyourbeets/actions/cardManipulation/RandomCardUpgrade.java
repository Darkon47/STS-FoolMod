package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class RandomCardUpgrade extends EYBActionWithCallback<AbstractCard>
{
    public RandomCardUpgrade()
    {
        super(ActionType.CARD_MANIPULATION);
    }

    @Override
    protected void FirstUpdate()
    {
        RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
        RandomizedList<AbstractCard> possible = new RandomizedList<>();

        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.canUpgrade() && !GameUtilities.IsCurseOrStatus(c))
            {
                if (!c.upgraded)
                {
                    betterPossible.Add(c);
                }
                else
                {
                    possible.Add(c);
                }
            }
        }

        if (betterPossible.Count() > 0)
        {
            card = betterPossible.Retrieve(AbstractDungeon.cardRng);
        }
        else if (possible.Count() > 0)
        {
            card = possible.Retrieve(AbstractDungeon.cardRng);
        }
        else
        {
            card = null;
        }

        if (card != null)
        {
            card.upgrade();
            card.flash();
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone && card != null)
        {
            Complete(card);
        }
    }
}
