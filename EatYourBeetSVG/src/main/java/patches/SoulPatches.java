package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SoulPatches
{
    @SpirePatch(clz = Soul.class, method = "discard", paramtypez = {AbstractCard.class, boolean.class})
    public static class SoulPatches_Discard
    {
        @SpirePostfixPatch
        public static void Postfix(Soul soul, AbstractCard card, boolean visualOnly)
        {
            if (card != null && !visualOnly)
            {
                if (card.tags.contains(AbstractEnums.CardTags.LOYAL))
                {
                    soul.isReadyForReuse = true;
                    AbstractDungeon.player.discardPile.moveToDeck(card, true);
                }
                else if (card.tags.contains(AbstractEnums.CardTags.PURGE))
                {
                    soul.isReadyForReuse = true;
                    AbstractDungeon.player.discardPile.removeCard(card);
                    card.tags.remove(AbstractEnums.CardTags.PURGE);
                }
            }
        }
    }

    @SpirePatch(clz = Soul.class, method = "obtain", paramtypez = {AbstractCard.class})
    public static class SoulPatches_Obtain
    {
        public static void Postfix(Soul soul, AbstractCard card)
        {
            if (card.tags.contains(AbstractEnums.CardTags.UNIQUE))
            {
                AbstractCard first = null;

                ArrayList<AbstractCard> toRemove = new ArrayList<>();

                ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
                for (AbstractCard c : cards)
                {
                    if (c.cardID.equals(card.cardID))
                    {
                        if (first == null)
                        {
                            first = c;
                        }
                        else
                        {
                            toRemove.add(c);
                            for (int i = 0; i <= c.timesUpgraded; i++)
                            {
                                first.upgrade();
                            }
                        }
                    }
                }

                for (AbstractCard c : toRemove)
                {
                    cards.remove(c);
                }
            }
        }
    }
}

