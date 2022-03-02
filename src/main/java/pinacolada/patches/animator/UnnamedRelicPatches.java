package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.*;
import eatyourbeets.cards.animator.special.SogaNoTojiko;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.effects.special.UnnamedRelicEquipEffect;
import eatyourbeets.utilities.FieldInfo;
import javassist.CtBehavior;
import pinacolada.utilities.PCLJUtils;

public class UnnamedRelicPatches
{
    private final static FieldInfo<EYBCardData> _data = PCLJUtils.GetField("customApparition", UnnamedRelicEquipEffect.class);

    @SpirePatch(
            clz= UnnamedRelicEquipEffect.class,
            method="update"
    )
    public static class TempFixToPreventExplosions
    {
        @SpireInsertPatch(
                locator= Locator.class
        )
        public static void Insert(UnnamedRelicEquipEffect __instance)
        {
            _data.Set(__instance, SogaNoTojiko.DATA);
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TheUnnamedReign.class, "GetCardReplacements");
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] - 1};
            }
        }
    }

}
