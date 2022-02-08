package pinacolada.actions.special;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import pinacolada.monsters.PCLAlly;

public class SummonAction extends EYBActionWithCallback<PCLAlly>
{
    protected PCLAlly ally;
    protected int slot;

    public SummonAction(PCLAlly ally)
    {
        this(ally, CombatStats.Dolls.GetAvailableSlot());
    }

    public SummonAction(PCLAlly ally, int position)
    {
        super(ActionType.SPECIAL);
        this.ally = ally;
        this.slot = position;
        this.ally.Slot = this.slot;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.slot < 0 || this.slot >= CombatStats.Dolls.MaxSlots) {
            Complete();
        }

        CombatStats.Dolls.Slots[this.slot] = this.ally;
        this.ally.Visible = true;
        this.ally.rollMove();
        this.ally.showHealthBar();
        this.ally.healthBarUpdatedEvent();

        Complete(ally);
    }
}
