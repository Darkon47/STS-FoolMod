package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

@SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
public class EmptyDeckShuffleAction_Update
{
    private static final FieldInfo<Boolean> _shuffled = JavaUtilities.GetField("shuffled", EmptyDeckShuffleAction.class);

    @SpirePrefixPatch
    public static void Prefix(EmptyDeckShuffleAction __instance)
    {
        if (!__instance.isDone && !_shuffled.Get(__instance))
        {
            PlayerStatistics.OnShuffle(true);
        }
    }
}
