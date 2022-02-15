package pinacolada.patches.screens;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.mainMenu.SaveSlotScreen;
import pinacolada.resources.PGR;

public class SaveSlotScreenPatches
{
    @SpirePatch(clz = SaveSlotScreen.class, method = "confirm", paramtypez = {int.class})
    public static class SaveSlotScreenPatches_Confirm
    {
        @SpirePostfixPatch
        public static void Postfix(SaveSlotScreen __instance, int slot)
        {
            PGR.PCL.Config.Load(slot);
            PGR.Fool.Data.Reload();
        }
    }
}
