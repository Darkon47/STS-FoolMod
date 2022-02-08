package pinacolada.patches.screens;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.utilities.PCLJUtils;

public class CardLibraryScreenPatches
{
    private static final FieldInfo<AbstractCard> hoveredCards = PCLJUtils.GetField("hoveredCard", CardLibraryScreen.class);
    private static final FieldInfo<ColorTabBar> _colorBar = PCLJUtils.GetField("colorBar", CardLibraryScreen.class);
    private static GUI_Button openButton;
    @SpirePatch(clz = CardLibraryScreen.class, method = "open")
    public static class CardLibraryScreen_Open
    {

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen screen)
        {
            ColorTabBar tabBar = _colorBar.Get(screen);
            if (tabBar.curTab != ColorTabBarFix.Enums.MOD)
            {
                screen.didChangeTab(tabBar, tabBar.curTab = ColorTabBarFix.Enums.MOD);
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "didChangeTab", paramtypez = {ColorTabBar.class, ColorTabBar.CurrentTab.class})
    public static class CardLibraryScreen_DidChangeTab
    {
        private static final FieldInfo<CardLibSortHeader> _sortHeader = PCLJUtils.GetField("sortHeader", CardLibraryScreen.class);
        private static CardLibSortHeader defaultHeader;

        @SpireInsertPatch(rloc = 0)
        public static void Insert(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection)
        {
            if (!IsAnimator(screen)) {
                Hitbox upgradeHitbox = tabBar.viewUpgradeHb;
                upgradeHitbox.width = 260 * Settings.scale;
                if (_sortHeader.Get(screen) != PGR.UI.CustomHeader)
                {
                    _sortHeader.Set(screen, PGR.UI.CustomHeader);
                }

                PGR.UI.CustomHeader.SetupButtons(!(newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(PGR.Enums.Cards.THE_FOOL)));
            }
        }

        @SpirePostfixPatch
        public static void Postfix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) {
            PGR.UI.CardFilters.Initialize(__ -> PGR.UI.CustomHeader.UpdateForFilters(), PGR.UI.CustomHeader.originalGroup, PGR.UI.CustomHeader.IsColorless());
            PGR.UI.CustomHeader.UpdateForFilters();
            if (openButton == null) {
                openButton = new GUI_Button(PGR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                        .SetBorder(PGR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                        .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(PGR.PCL.Strings.Misc.Filters)
                        .SetOnClick(() -> {
                            if (PGR.UI.CardFilters.isActive) {
                                PGR.UI.CardFilters.Close();
                            }
                            else {
                                PGR.UI.CardFilters.Open();
                            }
                        })
                        .SetColor(Color.GRAY);
            }
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="update")
    public static class CardLibraryScreen_Update
    {
        private static FieldInfo<Boolean> _grabbedScreen = PCLJUtils.GetField("grabbedScreen", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen __instance)
        {
            if (!PGR.UI.CardFilters.isActive && openButton != null && !IsAnimator(__instance)) {
                openButton.TryUpdate();
            }
            if (PGR.UI.CardFilters.TryUpdate())
            {
                _grabbedScreen.Set(__instance, false);
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "updateScrolling")
    public static class CardLibraryScreen_UpdateScrolling
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(CardLibraryScreen __instance)
        {
            if (PGR.UI.CardFilters.TryUpdate()) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="render", paramtypez = {SpriteBatch.class})
    public static class CardLibraryScreen_Render
    {
        @SpirePrefixPatch
        public static void Postfix(CardLibraryScreen __instance, SpriteBatch sb)
        {
            if (!PGR.UI.CardFilters.isActive && openButton != null && !IsAnimator(__instance)) {
                openButton.TryRender(sb);
            }
        }
    }

    protected static boolean IsAnimator(CardLibraryScreen screen) {
        ColorTabBar tabBar = _colorBar.Get(screen);
        return tabBar != null && tabBar.curTab == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(eatyourbeets.resources.GR.Enums.Cards.THE_ANIMATOR);
    }
}