package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.TupleT2;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class AirOrbEvokeAction extends EYBAction
{
    private final int hits;

    public AirOrbEvokeAction(int damage, int hits)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        Initialize(damage);
        this.hits = hits;
    }

    @Override
    protected void FirstUpdate()
    {
        if (PCLGameUtilities.GetEnemies(true).size() > 0) {
            SFX.Play(SFX.PCL_ORB_AIR_EVOKE);
            PCLGameEffects.Queue.Add(VFX.Whirlwind());
            int[] damage = PCLGameUtilities.CreateDamageMatrix(amount, true, new TupleT2<>(LockOnPower.POWER_ID, 1.5f));
            for (int i = 0; i < hits; i++) {
                PCLActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
                        .SetPCLAttackType(PCLAttackType.Air, true)
                        .SetDamageEffect((enemy, __) -> PCLGameEffects.List.Add(VFX.Tornado(enemy.hb)))
                        .SetVFX(true, false);
            }
        }
        Complete();
    }
}