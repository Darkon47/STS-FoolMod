package pinacolada.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class DealDamageToAll extends EYBActionWithCallback<ArrayList<AbstractCreature>>
{
    public final int[] damage;

    protected final ArrayList<AbstractCreature> targets = new ArrayList<>();
    protected BiConsumer<AbstractCreature, Boolean> onDamageEffect;
    protected boolean applyPowers;
    protected boolean applyPowerRemovalMultiplier;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean isFast;
    protected PCLAttackType pclAttackType = PCLAttackType.Normal;

    protected Color vfxColor = null;
    protected Color enemyTint = null;
    protected float pitchMin = 0.95f;
    protected float pitchMax = 1.05f;

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AttackEffect attackEffect)
    {
        this(source, amount, damageType, attackEffect, false);
    }

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AttackEffect attackEffect, boolean isFast)
    {
        super(ActionType.DAMAGE, isFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.attackEffect = attackEffect;
        this.damageType = damageType;
        this.damage = amount;

        Initialize(source, null, amount[0]);
    }

    public DealDamageToAll ApplyPowers(boolean applyPowers)
    {
        this.applyPowers = applyPowers;
        return this;
    }

    public DealDamageToAll SetDamageEffect(String effekseerKey)
    {
        this.onDamageEffect = (m, __) -> VFX.EFX(effekseerKey, m.hb);
        return this;
    }

    public DealDamageToAll SetDamageEffect(BiConsumer<AbstractCreature, Boolean> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamageToAll SetPiercing(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamageToAll SetVFXColor(Color color)
    {
        this.vfxColor = color.cpy();

        return this;
    }

    public DealDamageToAll SetVFXColor(Color color, Color enemyTint)
    {
        this.vfxColor = color.cpy();
        this.enemyTint = enemyTint.cpy();

        return this;
    }

    public DealDamageToAll SetSoundPitch(float pitchMin, float pitchMax)
    {
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;

        return this;
    }

    public DealDamageToAll SetVFX(boolean superFast, boolean muteSfx)
    {
        this.isFast = superFast;

        if (muteSfx)
        {
            this.pitchMin = this.pitchMax = 0;
        }

        return this;
    }

    public DealDamageToAll SetPCLAttackType(PCLAttackType pclAttackType)
    {
        this.pclAttackType = pclAttackType;

        return this;
    }

    public DealDamageToAll SetPCLAttackType(PCLAttackType pclAttackType, boolean applyPowerRemovalMultiplier)
    {
        this.pclAttackType = pclAttackType;
        this.applyPowerRemovalMultiplier = applyPowerRemovalMultiplier;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        boolean mute = pitchMin == 0;
        int i = 0;
        for (AbstractMonster enemy : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (!PCLGameUtilities.IsDeadOrEscaped(enemy))
            {
                if (mute)
                {
                    PCLGameEffects.List.AttackWithoutSound(source, enemy, this.attackEffect, vfxColor, 0.15f);
                }
                else
                {
                    PCLGameEffects.List.Attack(source, enemy, this.attackEffect, pitchMin, pitchMax, vfxColor, 0.15f);
                }

                if (onDamageEffect != null)
                {
                    onDamageEffect.accept(enemy, !mute);
                }

                if (enemy.powers != null) {
                    for (AbstractPower po : enemy.powers) {
                        if (pclAttackType.reactionPowers.contains(po.ID)) {
                            if (po instanceof PCLTriggerablePower) {
                                PCLActions.Last.AddPowerEffectEnemyBonus(po.ID, ((PCLTriggerablePower) po).reactionIncrease);
                                PCLActions.Last.ReducePower(po, 1);
                            }
                        }
                    }
                }


                mute = true;
            }

            i += 1;
        }

        AddDuration(AttackEffects.GetDamageDelay(attackEffect));
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            for (AbstractPower p : player.powers)
            {
                p.onDamageAllEnemies(this.damage);
            }

            int i = 0;
            for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(false))
            {
                if (!PCLGameUtilities.IsDeadOrEscaped(enemy))
                {
                    final DamageInfo info = new DamageInfo(this.source, this.damage[i], this.damageType);
                    if (applyPowers) {
                        info.applyPowers(source, enemy);
                    }
                    if (applyPowerRemovalMultiplier && enemy.powers != null) {
                        for (AbstractPower po : enemy.powers) {
                            if (pclAttackType.reactionPowers.contains(po.ID)) {
                                info.output *= pclAttackType.GetDamageMultiplier(po.ID);
                            }
                        }
                    }
                    DamageHelper.ApplyTint(enemy, enemyTint, attackEffect);
                    DamageHelper.DealDamage(enemy, info, bypassBlock, bypassThorns);
                    targets.add(enemy);
                }

                i += 1;
            }

            if (PCLGameUtilities.GetCurrentRoom(true).monsters.areMonstersBasicallyDead())
            {
                PCLGameUtilities.ClearPostCombatActions();
            }

            if (!isFast && !Settings.FAST_MODE)
            {
                PCLActions.Top.Wait(0.1f);
            }

            Complete(targets);
        }
    }
}
