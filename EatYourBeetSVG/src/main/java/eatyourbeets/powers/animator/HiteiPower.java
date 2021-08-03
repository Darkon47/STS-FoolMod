package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.animator.HiteiAction;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class HiteiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HiteiPower.class);

    private int upgradeStack;
    private int unupgradedStacks;

    public HiteiPower(AbstractPlayer owner, boolean upgraded)
    {
        super(owner, POWER_ID);

        if (upgraded)
        {
            this.upgradeStack += 1;
        }
        else
        {
            this.unupgradedStacks = 1;
        }

        Initialize(1);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        HiteiPower other = JUtils.SafeCast(power, HiteiPower.class);
        if (other != null && power.owner == target)
        {
            this.unupgradedStacks += other.unupgradedStacks;
            this.upgradeStack += other.upgradeStack;
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        for (int i = 0; i < unupgradedStacks; i++)
        {
            GameActions.Bottom.Add(new HiteiAction(2));
        }

        for (int i = 0; i < upgradeStack; i++)
        {
            GameActions.Bottom.Add(new HiteiAction(3));
        }

        GameActions.Bottom.Draw(1);

        this.flash();
    }
}