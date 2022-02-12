package pinacolada.actions.special;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.animator.combat.UnnamedDollSlot;
import pinacolada.monsters.PCLAlly;

public class SummonAction extends EYBActionWithCallback<PCLAlly>
{
    protected PCLAlly ally;
    protected UnnamedDollSlot slot;

    public SummonAction(PCLAlly ally)
    {
        this(ally, CombatStats.Dolls.GetAvailableSlot());
    }

    public SummonAction(PCLAlly ally, int slotNumber)
    {
        this(ally, slotNumber < CombatStats.Dolls.Slots.length && slotNumber >= 0 ? CombatStats.Dolls.GetSlot(slotNumber) : null);
    }

    public SummonAction(PCLAlly ally, UnnamedDollSlot slot)
    {
        super(ActionType.SPECIAL);
        this.ally = ally;
        this.slot = slot;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.slot == null) {
            Complete();
        }

        this.slot.Doll = this.ally;
        this.ally.Visible = true;
        this.ally.rollMove();
        this.ally.showHealthBar();
        this.ally.healthBarUpdatedEvent();

        Complete(ally);
    }
}
