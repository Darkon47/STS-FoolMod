package pinacolada.cards.base.baseeffects;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseEffect_Affinity extends BaseEffect {
    protected ArrayList<PCLAffinity> affinities;

    public BaseEffect_Affinity(SerializedData content)
    {
        super(content);
        this.affinities = ParseAffinitiesFromEntityID();
    }

    public BaseEffect_Affinity(String ID, PCLCardTarget target, int amount, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, Enum::name), PCLCardTarget.Self, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
    }

    public BaseEffect_Affinity Set(PCLAffinity... affinities) {
        return Set(Arrays.asList(affinities));
    }

    public BaseEffect_Affinity Set(List<PCLAffinity> affinities) {
        this.affinities.clear();
        this.affinities.addAll(affinities);
        this.entityID = JoinEntityIDs(affinities, af -> af.Name);
        return this;
    }

    public BaseEffect_Affinity Add(PCLAffinity newAf) {
        if (newAf != null) {
            this.affinities.add(newAf);
            this.entityID = JoinEntityIDs(affinities, Enum::name);
        }
        return this;
    }

    public ArrayList<PCLAffinity> GetAffinities() {
        return affinities;
    }
}
