package pinacolada.patches.energyPanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import pinacolada.ui.common.ControllableCardPile;
import pinacolada.utilities.PCLGameUtilities;

public class EnergyPanelPatches
{
    public static ControllableCardPile Pile;

    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class EnergyPanelPatches_Update
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance)
        {
            if (PCLGameUtilities.InBattle() && Pile != null)
            {
                Pile.Update(__instance);
            }
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class EnergyPanelPatches_Render
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance, SpriteBatch sb)
        {
            if (PCLGameUtilities.InBattle() && Pile != null)
            {
                Pile.Render(__instance, sb);
            }
        }
    }
}