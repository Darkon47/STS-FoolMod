package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.beta.WinryRockbell;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;

public class WinryRockbellPower extends AnimatorPower implements OnAfterCardDiscardedSubscriber
{
    public static final int BLOCK_AMOUNT = 4;

    private int baseAmount;

    public WinryRockbellPower(AbstractCreature owner, int amount)
    {
        super(owner, WinryRockbell.DATA);

        this.amount = amount;
        this.baseAmount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PlayerStatistics.onAfterCardDiscarded.Subscribe(this);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        this.baseAmount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        this.amount = baseAmount;
        updateDescription();
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (owner == null || !owner.powers.contains(this))
        {
            PlayerStatistics.onAfterCardDiscarded.Unsubscribe(this);
            return;
        }

        if (enabled)
        {
            GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
            this.flashWithoutSound();
            this.amount -= 1;
        }

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, BLOCK_AMOUNT);
        this.enabled = (amount > 0);
    }
}
