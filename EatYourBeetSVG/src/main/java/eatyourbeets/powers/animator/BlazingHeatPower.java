package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;

public class BlazingHeatPower extends AnimatorPower implements OnApplyPowerSubscriber
{
    public static final String POWER_ID = CreateFullID(BlazingHeatPower.class);

    public BlazingHeatPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Fire.ORB_ID.equals(orb.ID)) {
            if (owner.isPlayer)
            {
                int[] damageMatrix = DamageInfo.createDamageMatrix(orb.passiveAmount * this.amount, true);
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
            }
            else {
                GameActions.Bottom.DealDamage(null, player, orb.passiveAmount * this.amount, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
            }
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (BurningPower.POWER_ID.equals(power.ID) && target.hasPower(FreezingPower.POWER_ID) && source == owner) {
            GameActions.Bottom.DealDamage(null, player, ((FreezingPower) target.getPower(FreezingPower.POWER_ID)).GetPassiveDamage(), DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
        }
    }
}
