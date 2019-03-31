package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import eatyourbeets.Utilities;
import eatyourbeets.subscribers.OnRemoveFromDeckSubscriber;

@SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
public class OnRemoveCardPatch
{
    public static void Postfix(CardGroup __instance, AbstractCard c)
    {
        if (__instance.type == CardGroupType.MASTER_DECK)
        {
            OnRemoveFromDeckSubscriber card = Utilities.SafeCast(c, OnRemoveFromDeckSubscriber.class);
            if (card != null)
            {
                card.OnRemoveFromDeck();
            }
        }
    }
}
