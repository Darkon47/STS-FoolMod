package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class BozesPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BozesPower.class.getSimpleName());

    private final static int PLATED_ARMOR = 1;

    public BozesPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];// + PLATED_ARMOR + desc[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = Utilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            if (amount > 0)
            {
                GameActionsHelper.DrawCard(owner, 1);
                //GameActionsHelper.ApplyPower(owner, owner, new PlatedArmorPower(owner, PLATED_ARMOR), PLATED_ARMOR);
                amount -= 1;
                updateDescription();
                this.flash();
            }
        }
    }
}
