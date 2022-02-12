package pinacolada.relics.fool;

import pinacolada.powers.common.BurningWeaponPower;
import pinacolada.relics.FoolRelic;
import pinacolada.utilities.PCLActions;

public class FlametangBoomerang extends FoolRelic
{
    public static final String ID = CreateFullID(FlametangBoomerang.class);

    public FlametangBoomerang()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        PCLActions.Bottom.StackPower(new BurningWeaponPower(player , 2));
    }
}