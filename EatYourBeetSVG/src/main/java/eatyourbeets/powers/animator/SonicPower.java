package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.cards.animator.ThrowingKnife;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SonicPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SonicPower.class.getSimpleName());

    public SonicPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActionsHelper_Legacy.ApplyPower(owner, owner, new DexterityPower(owner, 1), 1);
        GameActionsHelper_Legacy.MakeCardInHand(ThrowingKnife.GetRandomCard(), 1, false);
        GameActionsHelper_Legacy.AddToBottom(new ReducePowerAction(owner, owner, this, 1));
    }
}