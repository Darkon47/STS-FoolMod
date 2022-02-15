package pinacolada.cards.fool.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Air extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Air.class, PCLOrbHelper.Air)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColorless();

    public OrbCore_Air()
    {
        super(DATA, 1,6);

        SetAffinity_Green(1);
    }
}