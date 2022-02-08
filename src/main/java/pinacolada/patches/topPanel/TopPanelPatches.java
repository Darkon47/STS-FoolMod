package pinacolada.patches.topPanel;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.potions.PCLPotion;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class TopPanelPatches {

    @SpirePatch(clz= TopPanel.class, method="update")
    public static class TopPanelPatches_Update
    {
        @SpirePrefixPatch
        public static SpireReturn Method(TopPanel __instance)
        {
            // To simulate AbstractDungeon.screen == CurrentScreen.NO_INTERACT
            if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN || Settings.hideTopBar)
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz= TopPanel.class, method="renderPotionTips")
    public static class TopPanel_RenderPotionTips
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"p"})
        public static SpireReturn<Void> Insert(TopPanel __instance, AbstractPotion p)
        {
            PCLPotion po = PCLJUtils.SafeCast(p, PCLPotion.class);
            if (po != null) {
                PCLCardTooltip.QueueTooltips(po);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "queuePowerTips");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
