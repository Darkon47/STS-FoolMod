package pinacolada.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.TupleT2;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Air;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class AirOrbPassiveAction extends EYBAction
{
    protected final Air orb;

    public AirOrbPassiveAction(Air orb, int damage)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        this.orb = orb;

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        SFX.Play(SFX.ATTACK_WHIRLWIND);
        PCLGameEffects.Queue.Add(VFX.Whirlwind());
        PCLGameEffects.Queue.Add(VFX.RazorWind(orb.hb, player.hb, MathUtils.random(1000.0F, 1200.0F), MathUtils.random(-20.0F, 20.0F)));
        int[] damage = PCLGameUtilities.CreateDamageMatrix(amount, true, new TupleT2<>(LockOnPower.POWER_ID, 1.5f));
        PCLActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                .SetPCLAttackType(PCLAttackType.Air, true)
        .SetVFX(true, false);

        Complete();
    }
}