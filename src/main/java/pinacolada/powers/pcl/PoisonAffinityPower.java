package pinacolada.powers.pcl;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import pinacolada.actions.powers.ApplyPower;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class PoisonAffinityPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(PoisonAffinityPower.class);

    public PoisonAffinityPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (PCLGameUtilities.IsPlayer(source) && power.ID.equals(PoisonPower.POWER_ID))
        {
            power.amount += this.amount;

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += this.amount;
            }
            else
            {
                PCLJUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }
}
