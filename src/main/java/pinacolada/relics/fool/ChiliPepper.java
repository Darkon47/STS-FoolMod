package pinacolada.relics.fool;

import eatyourbeets.utilities.TargetHelper;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class ChiliPepper extends PCLRelic
{
    public static final String ID = CreateFullID(ChiliPepper.class);
    public static final int AMOUNT = 2;

    public ChiliPepper()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }


    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();
        PCLActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(), AMOUNT);
        flash();
    }

    @Override
    public String getUpdatedDescription()
    {
        return PCLJUtils.Format(DESCRIPTIONS[0], AMOUNT);
    }
}