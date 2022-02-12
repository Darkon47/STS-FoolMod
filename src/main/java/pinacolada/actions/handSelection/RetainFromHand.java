package pinacolada.actions.handSelection;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class RetainFromHand extends SelectFromHand
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public RetainFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.EXHAUST, sourceName, amount, isRandom);
    }

    public RetainFromHand ShowEffect(boolean showEffect, boolean realtime)
    {
        this.showEffect = showEffect;
        this.realtime = realtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            PCLGameUtilities.Retain(card);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(RetainCardsAction.TEXT[0]);
    }
}
