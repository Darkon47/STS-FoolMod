package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.characters.AnimatorCharacterSelect;
import eatyourbeets.utilities.GameUtilities;

public class VictoryPatches
{
    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryScreenPatches_createGameOverStats
    {
        @SpirePrefixPatch
        public static void Method(VictoryScreen __instance)
        {
            if (Settings.isStandardRun() && AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR)
            {
                AnimatorCharacterSelect.OnTrueVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }

    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class VictoryRoomPatches_onEnterRoom
    {
        @SpirePrefixPatch
        public static void Method(VictoryRoom __instance)
        {
            if (__instance.eType == VictoryRoom.EventType.HEART && Settings.isStandardRun()
                && AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR)
            {
                AnimatorCharacterSelect.OnVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }
}
