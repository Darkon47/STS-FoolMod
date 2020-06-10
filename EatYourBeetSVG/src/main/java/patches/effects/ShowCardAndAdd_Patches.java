package patches.effects;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class ShowCardAndAdd_Patches
{
    public static void TriggerWhenCreated(AbstractCard card)
    {
        if (card instanceof EYBCard)
        {
            ((EYBCard)card).triggerWhenCreated(false);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
    public static class ShowCardAndAddToDrawPileEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToDrawPileEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance)
        {
            if (__instance.isDone)
            {
                TriggerWhenCreated(_card.Get(__instance));
            }
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    public static class ShowCardAndAddToDiscardPileEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToDiscardEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDiscardEffect __instance)
        {
            if (__instance.isDone)
            {
                TriggerWhenCreated(_card.Get(__instance));
            }
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
    public static class ShowCardAndAddToHandEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToHandEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToHandEffect __instance)
        {
            if (__instance.isDone)
            {
                TriggerWhenCreated(_card.Get(__instance));
            }
        }
    }
}
