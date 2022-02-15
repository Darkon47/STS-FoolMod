package pinacolada.resources;

import com.megacrit.cardcrawl.random.Random;
import pinacolada.resources.pcl.misc.PCLLoadout;

import java.util.ArrayList;

public abstract class PCLAbstractPlayerData {
    public final ArrayList<PCLLoadout> BaseLoadouts = new ArrayList<>();
    public final ArrayList<PCLLoadout> BetaLoadouts = new ArrayList<>();

    public void Initialize() {}
    public void InitializeCardPool(boolean startGame) {}

    public final Random GetRNG()
    {
        return PGR.PCL.Dungeon.GetRNG();
    }
}
