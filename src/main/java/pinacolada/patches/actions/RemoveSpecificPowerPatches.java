package pinacolada.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.utilities.PCLGameUtilities;

public class RemoveSpecificPowerPatches {

    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
            {AbstractCreature.class, AbstractCreature.class, String.class})
    public static class RemoveSpecificPowerAction_Ctor
    {
        @SpirePostfixPatch
        public static void Postfix(RemoveSpecificPowerAction __instance, AbstractCreature target, AbstractCreature source,
                                   String powerToRemove)
        {
            if (!PCLGameUtilities.CanReducePower(source, target, powerToRemove, __instance))
            {
                __instance.isDone = true;
            }
        }
    }

    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
            {AbstractCreature.class, AbstractCreature.class, AbstractPower.class})
    public static class RemoveSpecificPowerAction_Ctor2
    {
        @SpirePostfixPatch
        public static void Postfix(RemoveSpecificPowerAction __instance, AbstractCreature target, AbstractCreature source,
                                   AbstractPower powerInstance)
        {
            if (!PCLGameUtilities.CanReducePower(source, target, powerInstance, __instance))
            {
                __instance.isDone = true;
            }
        }
    }
}


