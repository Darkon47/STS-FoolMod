package pinacolada.patches;

import basemod.DevConsole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;

public class DevConsolePatches
{
    @SpirePatch(clz = DevConsole.class, method = "execute")
    public static class AutocompletePatches_Execute
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            if (PCLGameUtilities.InGame() && PCLGameUtilities.IsPCLPlayerClass())
            {
                PGR.PCL.Dungeon.SetCheating();
            }
        }
    }
}
