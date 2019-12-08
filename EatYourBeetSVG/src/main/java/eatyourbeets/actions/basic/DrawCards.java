package eatyourbeets.actions.basic;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.actions.utility.SequentialAction;
import eatyourbeets.utilities.GameActionsHelper2;

import java.util.ArrayList;
import java.util.function.Predicate;

public class DrawCards extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> cards = new ArrayList<>();
    protected Predicate<AbstractCard> filter;
    protected boolean canDrawUnfiltered;
    protected boolean shuffleIfEmpty;

    protected DrawCards(DrawCards other, int amount)
    {
        this(amount, other.shuffleIfEmpty);

        filter = other.filter;
        canDrawUnfiltered = other.canDrawUnfiltered;
        callbacks.addAll(other.callbacks);
        cards.addAll(other.cards);
    }

    public DrawCards(int amount)
    {
        this(amount, true);
    }

    public DrawCards(int amount, boolean shuffleIfEmpty)
    {
        super(ActionType.DRAW);

        this.shuffleIfEmpty = shuffleIfEmpty;

        Initialize(amount);
    }

    public DrawCards SetFilter(Predicate<AbstractCard> filter, boolean canDrawUnfiltered)
    {
        this.filter = filter;
        this.canDrawUnfiltered = canDrawUnfiltered;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.hasPower(NoDrawPower.POWER_ID))
        {
            player.getPower(NoDrawPower.POWER_ID).flash();
            Complete(cards);
            return;
        }

        if (amount == 0)
        {
            Complete(cards);
            return;
        }

        if (player.drawPile.isEmpty())
        {
            if (shuffleIfEmpty && !player.discardPile.isEmpty())
            {
                GameActionsHelper2.AddToTop(new SequentialAction(
                    new DrawCards(this, amount),
                    new EmptyDeckShuffleAction()
                ));

                Complete(); // Do not trigger callback
            }
            else
            {
                Complete(cards);
            }
        }
        else if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            player.createHandIsFullDialog();
            Complete(cards);
        }
        else
        {
            if (filter != null)
            {
                AbstractCard filtered = null;
                for (AbstractCard card : player.drawPile.group)
                {
                    if (filter.test(card))
                    {
                        filtered = card;
                        break;
                    }
                }

                if (filtered != null)
                {
                    player.drawPile.removeCard(filtered);
                    player.drawPile.addToTop(filtered);
                }
                else if (!canDrawUnfiltered)
                {
                    Complete(cards);
                    return;
                }
            }

            cards.add(player.drawPile.getTopCard());

            GameActionsHelper2.AddToTop(new SequentialAction(
                    new DrawCardAction(source, 1, false),
                    new DrawCards(this, amount - 1)
            ));

            Complete(); // Do not trigger callback
        }
    }
}
