package eatyourbeets.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.animator.AnimatorAction;

public class DrawSpecificCardAction extends AnimatorAction
{
    private final AbstractPlayer player;
    private final AbstractCard card;

    public DrawSpecificCardAction(AbstractCard card)
    {
        this.card = card;
        this.player = AbstractDungeon.player;

        this.setValues(this.player, AbstractDungeon.player);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            this.player.createHandIsFullDialog();
        }
        else
        {
            if (this.player.drawPile.contains(card))
            {
                this.player.drawPile.removeCard(card);
                this.player.drawPile.addToTop(card);
                this.player.draw(1);

//                card.triggerWhenDrawn();
//                card.lighten(false);
//                this.player.hand.addToHand(card);
//
//                this.player.drawPile.removeCard(card);
//                this.player.onCardDrawOrDiscard();
//                this.player.hand.refreshHandLayout();
//                this.player.hand.glowCheck();
            }
        }

        this.isDone = true;
    }
}
