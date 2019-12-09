package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("UnusedReturnValue")
public class GameActionsHelperBase
{
    public static final GameActions Top = new GameActions(Order.Top);
    public static final GameActions Bottom = new GameActions(Order.Bottom);

    public enum Order
    {
        Top,
        Bottom,
        TurnStart,
        NextCombat
    }

    protected static final Logger logger = GameUtilities.GetLogger(GameActionsHelperBase.class);
    protected static Order actionOrder = Order.Bottom;

    public static void ClearPostCombatActions()
    {
        AbstractDungeon.actionManager.clearPostCombatActions();
    }

    public static void ResetOrder()
    {
        SetOrderBottom();
    }

    public static void SetOrderBottom()
    {
        SetOrder(Order.Bottom);
    }

    public static void SetOrderTop()
    {
        SetOrder(Order.Top);
    }

    public static void SetOrder(Order order)
    {
        actionOrder = order;
    }

    public static <T extends AbstractGameAction> T AddToDefault(T action)
    {
        switch (actionOrder)
        {
            case Top:
                AbstractDungeon.actionManager.addToTop(action);
                break;

            case Bottom:
                AbstractDungeon.actionManager.addToBottom(action);
                break;

            case TurnStart:
                AbstractDungeon.actionManager.addToTurnStart(action);
                break;

            case NextCombat:
                AbstractDungeon.actionManager.addToNextCombat(action);
                break;
        }

        return action;
    }

    public static <T extends AbstractGameAction> T AddToTurnStart(T action)
    {
        AbstractDungeon.actionManager.addToTurnStart(action);

        return action;
    }

    public static <T extends AbstractGameAction> T AddToTop(T action)
    {
        AbstractDungeon.actionManager.addToTop(action);

        return action;
    }

    public static <T extends AbstractGameAction> T AddToBottom(T action)
    {
        AbstractDungeon.actionManager.addToBottom(action);

        return action;
    }

    public static void PlayCard(AbstractCard card, AbstractMonster m)
    {
        if (card.cost > 0)
        {
            card.freeToPlayOnce = true;
        }

        AbstractDungeon.player.limbo.group.add(card);

        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;

        if (!card.canUse(AbstractDungeon.player, m))
        {
            AddToTop(new UnlimboAction(card));
            AddToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
            AddToTop(new WaitAction(0.4F));
        }
        else
        {
            card.applyPowers();
            card.freeToPlayOnce = true;

            AddToTop(new QueueCardAction(card, m));
            AddToTop(new UnlimboAction(card));

            if (!Settings.FAST_MODE)
            {
                AddToTop(new WaitAction(Settings.ACTION_DUR_MED));
            }
            else
            {
                AddToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }

    public static CardQueueItem PlayCopy(AbstractCard source, AbstractMonster m, boolean applyPowers)
    {
        AbstractCard temp = source.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(temp);
        temp.current_x = source.current_x;
        temp.current_y = source.current_y;
        temp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        temp.target_y = (float) Settings.HEIGHT / 2.0F;

        if (temp.cost > 0)
        {
            temp.freeToPlayOnce = true;
        }

        if (applyPowers)
        {
            temp.applyPowers();
        }

        temp.calculateCardDamage(m);
        temp.purgeOnUse = true;

        CardQueueItem item = new CardQueueItem(temp, m, source.energyOnUse, true);
        AbstractDungeon.actionManager.cardQueue.add(item);

        return item;
    }
}
