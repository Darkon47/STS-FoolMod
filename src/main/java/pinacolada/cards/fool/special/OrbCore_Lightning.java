package pinacolada.cards.fool.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Lightning extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Lightning.class, PCLOrbHelper.Lightning)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColorless();

    public OrbCore_Lightning()
    {
        super(DATA, 1, 4);

        SetAffinity_Light(1);
    }
}