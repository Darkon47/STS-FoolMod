package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.AnimatorCard_UltraRare;

public class CardLibraryPatches
{
    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
    public static class CardLibraryPatches_getCard1
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(String key)
        {
            AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
            if (card != null)
            {
                return SpireReturn.Return(card);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {AbstractPlayer.PlayerClass.class, String.class})
    public static class CardLibraryPatches_getCard2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractPlayer.PlayerClass playerClass, String key)
        {
            AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
            if (card != null)
            {
                return SpireReturn.Return(card);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }
}