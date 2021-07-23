package eatyourbeets.powers.affinity;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.common.VitalityPower;
import eatyourbeets.utilities.GameActions;

public class BlessingPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(BlessingPower.class);
    public static final AffinityType AFFINITY_TYPE = AffinityType.Light;

    protected static final int[] THRESHOLDS = new int[]{ 4, 8, 12, 16 };

    public BlessingPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
    }

    @Override
    protected void OnThresholdReached(int threshold)
    {
        GameActions.Bottom.StackPower(new VitalityPower(owner, 1));
    }
}