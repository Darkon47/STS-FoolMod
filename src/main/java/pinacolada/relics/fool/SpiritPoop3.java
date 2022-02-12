package pinacolada.relics.fool;

import pinacolada.powers.common.InspirationPower;
import pinacolada.relics.FoolRelic;
import pinacolada.utilities.PCLActions;

public class SpiritPoop3 extends FoolRelic
{
    public static final String ID = CreateFullID(SpiritPoop3.class);

    public SpiritPoop3()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        PCLActions.Bottom.StackPower(new InspirationPower(player, 2));
    }
}