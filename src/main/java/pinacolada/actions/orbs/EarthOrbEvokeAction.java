package pinacolada.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.Projectile;
import eatyourbeets.effects.card.RenderProjectilesEffect;
import eatyourbeets.utilities.Mathf;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

import java.util.ArrayList;

public class EarthOrbEvokeAction extends EYBAction
{
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final float throwDuration;
    private RenderProjectilesEffect effect;

    public EarthOrbEvokeAction(Earth earth, int damage)
    {
        super(ActionType.DAMAGE, 0.6f);

        this.throwDuration = Settings.FAST_MODE ? 0.15f : 0.25f;
        this.isRealtime = true;
        this.projectiles.addAll(earth.projectiles);
        earth.projectiles.clear();

        Initialize(damage);

        if (amount <= 0)
        {
            Complete();
            return;
        }

        final float w = earth.hb.width * 1.1f;
        final float h = earth.hb.height * 0.8f;
        final float angle = -MathUtils.random(40f, 80f) + (MathUtils.random(120f, 240f) / projectiles.size());
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = projectiles.get(i);
            p.SetTargetScale(p.GetScale(false) + 0.35f, null)
            .SetSpeed(33f, 35f, null, null)
            .SetTargetPosition(earth.cX + (earth.hb.width * 0.35f), earth.cY + (earth.hb.height * 3))
            .SetTargetOffset(w * Mathf.Cos(angle * i), h * Mathf.Sin(angle * i), null, null)
            .SetTargetRotation(p.target_pos.rotation + 36000, 210f + (i * 30));
        }
        PCLGameEffects.List.Add(effect = new RenderProjectilesEffect(projectiles, 999f, isRealtime));
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        effect.SetDuration(duration + (throwDuration * projectiles.size()), isRealtime);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (TickDuration(deltaTime))
        {
            SFX.Play(SFX.PCL_ORB_EARTH_EVOKE, 0.9f, 1.1f);

            for (int i = 0; i < projectiles.size(); i++)
            {
                PCLActions.Top.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AttackEffect.NONE)
                        .SetPCLAttackType(PCLAttackType.Earth, true)
                .SetOptions(true, true, true)
                .SetDamageEffect(m ->
                {
                    PCLGameEffects.List.Add(VFX.ThrowProjectile(projectiles.remove(0), m.hb)
                    .SetSpread(0.3f, 0.3f)
                    .AddCallback(hb -> PCLGameEffects.Queue.Add(VFX.RockBurst(hb, 1))))
                    .SetDuration(throwDuration, isRealtime);
                    return 0f;
                })
                .SetDuration(throwDuration, isRealtime);
            }

            Complete();
        }
    }
}
