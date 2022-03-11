package pinacolada.cards.base.baseeffects;

import pinacolada.cards.base.PCLCardTarget;
import pinacolada.orbs.PCLOrbHelper;

public abstract class BaseEffect_Orb extends BaseEffect {
    protected PCLOrbHelper orb;

    public BaseEffect_Orb(SerializedData content)
    {
        super(content);
        this.orb = PCLOrbHelper.Get(entityID);
    }

    public BaseEffect_Orb(String ID, PCLCardTarget target, int amount, PCLOrbHelper orb)
    {
        super(ID, orb != null ? orb.ID : null, PCLCardTarget.Self, amount);
        this.orb = orb;
    }

    public BaseEffect_Orb Set(PCLOrbHelper orb) {
        this.orb = orb;
        this.entityID = orb != null ? orb.ID : null;
        return this;
    }

    public PCLOrbHelper GetOrb() {
        return orb;
    }
}
