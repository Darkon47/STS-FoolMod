package pinacolada.powers.fool;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.ColoredString;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ElementalExposurePower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(ElementalExposurePower.class);
    public static final int ELEMENTAL_MODIFIER = 25;
    public static final int DECAY_TURNS = 1;
    public int secondaryAmount;

    public static float CalculatePercentage(AbstractCreature target)
    {
        return CalculatePercentage(PCLGameUtilities.GetPowerAmount(target, POWER_ID));
    }

    public static float CalculatePercentage(int amount)
    {
        return (100f + amount) / 100f;
    }

    public ElementalExposurePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.secondaryAmount = DECAY_TURNS;
        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            this.description = FormatDescription(0, amount, ELEMENTAL_MODIFIER, secondaryAmount);
        }
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(secondaryAmount, Color.WHITE, c.a);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_FIRE, 0.3f, 1.3f, 0.93f);
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.3f, 1.3f, 0.93f);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        this.secondaryAmount = DECAY_TURNS;
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        if (this.secondaryAmount <= 0) {
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
        else {
            this.secondaryAmount -= 1;
        }
    }
}