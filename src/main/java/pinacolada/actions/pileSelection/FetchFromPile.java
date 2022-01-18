package pinacolada.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import pinacolada.actions.basic.MoveCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class FetchFromPile extends SelectFromPile
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DRAW, sourceName, amount, groups);
    }

    public FetchFromPile ShowEffect(boolean showEffect, boolean isRealtime)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        final ArrayList<MoveCard> actions = new ArrayList<>();
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.hand);
            if (showEffect)
            {
                PCLActions.Top.Add(action).ShowEffect(showEffect, realtime);
            }
            else
            {
                actions.add(action);
            }
        }

        super.Complete(result);

        for (MoveCard action : actions)
        {
            action.update(); // only once
        }
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GR.PCL.Strings.GridSelection.Fetch_F1);
    }
}
