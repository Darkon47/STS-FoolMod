package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class SuperchargedPower extends PCLClickablePower
{
    public static final String POWER_ID = PGR.PCL.CreateID(SuperchargedPower.class.getSimpleName());
    public static final int COST = 10;
    public static final int BONUS = 5;

    public SuperchargedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Special, COST);
        this.triggerCondition
                .SetCheckCondition((c) -> this.amount >= COST)
                .SetPayCost(a -> {
                    ReducePower(COST);
                });
        this.triggerCondition.SetUses(-1, false, false);

        Initialize(amount);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);

        PCLActions.Bottom.GainEnergy(1);
        PCLActions.Delayed.AddPowerEffectBonus(ElectrifiedPower.POWER_ID, PCLCombatStats.Type.Effect, player.energy.energy * BONUS);
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0, COST, BONUS);
    }
}