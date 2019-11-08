package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.animator.EngravedStaff;
import eatyourbeets.utilities.GameActionsHelper;

public class ForcePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ForcePower.class.getSimpleName());

    public ForcePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, amount), amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, stackAmount), stackAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (amount <= 2 && PlayerStatistics.HasActivatedLimited(EngravedStaff.ID))
        {
            return;
        }

        if (PlayerStatistics.GetStrength() > 0)
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, 1));
        }

        GameActionsHelper.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }
}
