package pinacolada.cards.base.baseeffects;

import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseEffect_CardGroup extends BaseEffect {
    protected ArrayList<PCLCardGroupHelper> groupTypes;

    public BaseEffect_CardGroup(SerializedData content)
    {
        super(content);
        this.groupTypes = ParseCardGroupsFromEntityID();
    }

    public BaseEffect_CardGroup(String ID, PCLCardTarget target, int amount, PCLCardGroupHelper... groupTypes)
    {
        super(ID, JoinEntityIDs(groupTypes, c -> c.Name), PCLCardTarget.Self, amount);
        this.groupTypes = new ArrayList<>(Arrays.asList(groupTypes));
    }

    public BaseEffect_CardGroup Set(PCLCardGroupHelper... groupTypes) {
        return Set(Arrays.asList(groupTypes));
    }

    public BaseEffect_CardGroup Set(List<PCLCardGroupHelper> groupTypes) {
        this.groupTypes.clear();
        this.groupTypes.addAll(groupTypes);
        this.entityID = JoinEntityIDs(groupTypes, c -> c.Name);
        return this;
    }

    public BaseEffect_CardGroup Add(PCLCardGroupHelper newAf) {
        if (newAf != null) {
            this.groupTypes.add(newAf);
            this.entityID = JoinEntityIDs(groupTypes, c -> c.Name);
        }
        return this;
    }

    public ArrayList<PCLCardGroupHelper> GetGroups() {
        return groupTypes;
    }
}
