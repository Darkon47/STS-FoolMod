package eatyourbeets.powers.monsters;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActions;

public class LightningCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(LightningCrystalPower.class);

    public LightningCrystalPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");
        GameActions.Bottom.VFX(new LightningEffect(target.drawX, target.drawY));
        GameActions.Bottom.DealDamage(owner, target, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
        .SetPiercing(true, true);
    }
}