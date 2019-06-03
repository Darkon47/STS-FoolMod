package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class DarkWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarkWispPower.class.getSimpleName());

    public DarkWispPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
        {
            GameActionsHelper.ApplyPower(null, c, new ConstrictedPower(c, null, amount), amount);
        }
    }
}
