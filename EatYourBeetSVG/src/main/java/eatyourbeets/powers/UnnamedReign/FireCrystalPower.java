package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class FireCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FireCrystalPower.class.getSimpleName());

    public FireCrystalPower(AbstractCreature owner, int value)
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
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner)
        {
            this.flash();
            AbstractCreature target = PlayerStatistics.GetRandomCharacter(true);
            GameActionsHelper.ApplyPower(null, target, new StrengthPower(target, amount), amount);
        }

        return damageAmount;
    }
}
