package patches;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.ui.CustomCardLibSortHeader;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.Utilities;


@SpirePatch(clz=CardLibraryScreen.class, method="didChangeTab", paramtypez = {ColorTabBar.class, ColorTabBar.CurrentTab.class})
public class CardLibraryScreenPatches
{
    private static final Field<CardLibSortHeader> headerField = Utilities.GetPrivateField("sortHeader", CardLibraryScreen.class);
    private static final CustomCardLibSortHeader customHeader = new CustomCardLibSortHeader(null);
    private static CardLibSortHeader defaultHeader;

    @SpirePrefixPatch
    public static void Prefix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection)
    {
        if (defaultHeader == null)
        {
            defaultHeader = headerField.Get(screen);

            Hitbox upgradeHitbox = tabBar.viewUpgradeHb;

            float offsetX = 130 * Settings.scale;

            upgradeHitbox.width -= offsetX;
            //upgradeHitbox.move(upgradeHitbox.cX + (offsetX * 2), upgradeHitbox.cY);
        }

        if (newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(AbstractEnums.Cards.THE_ANIMATOR))
        {
            if (headerField.Get(screen) != customHeader)
            {
                headerField.Set(screen, customHeader);
                customHeader.SetupButtons();
            }
        }
        else
        {
            if (headerField.Get(screen) != defaultHeader)
            {
                headerField.Set(screen, defaultHeader);
            }
        }
    }
}