package pinacolada.relics.fool;

import pinacolada.relics.FoolRelic;

public class SpiritPoop2 extends FoolRelic
{
    public static final String ID = CreateFullID(SpiritPoop2.class);

    public SpiritPoop2()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }
}