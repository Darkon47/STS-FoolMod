package pinacolada.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.fool.StolenGoldPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class DealDamage extends EYBActionWithCallback<AbstractCreature>
{
    protected final DamageInfo info;

    protected FuncT1<Float, AbstractCreature> onDamageEffect;
    protected boolean applyPowerRemovalMultiplier;
    protected boolean applyPowers;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean canKill = true;
    protected boolean hasPlayedEffect;
    protected boolean isOrb;
    protected boolean skipWait;
    protected int goldAmount;
    protected PCLAttackType pclAttackType = PCLAttackType.Normal;

    protected Color vfxColor = null;
    protected Color enemyTint = null;
    protected float pitchMin = 0.95f;
    protected float pitchMax = 1.05f;

    protected DealDamage(AbstractCreature target, DealDamage other)
    {
        this(other.target, other.info, other.attackEffect);

        Import(other);

        this.card = other.card;
        this.vfxColor = other.vfxColor;
        this.enemyTint = other.enemyTint;
        this.pitchMin = other.pitchMin;
        this.pitchMax = other.pitchMax;
        this.skipWait = other.skipWait;
        this.applyPowers = other.applyPowers;
        this.bypassBlock = other.bypassBlock;
        this.bypassThorns = other.bypassThorns;
        this.onDamageEffect = other.onDamageEffect;
        this.hasPlayedEffect = other.hasPlayedEffect;
    }

    public DealDamage(AbstractCreature target, DamageInfo info)
    {
        this(target, info, AttackEffect.NONE);
    }

    public DealDamage(AbstractCard card, AbstractCreature target, AttackEffect effect)
    {
        this(card, target, new DamageInfo(AbstractDungeon.player, card.baseDamage, card.damageTypeForTurn), effect);
    }

    public DealDamage(AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        this(null, target, info, effect);
    }

    public DealDamage(AbstractCard card, AbstractCreature target, DamageInfo info, AttackEffect effect)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        this.card = card;
        this.goldAmount = 0;
        this.skipWait = false;
        this.info = info;
        this.attackEffect = effect;

        boolean isInvalid = target == null || PCLGameUtilities.IsDeadOrEscaped(target);
        Initialize(info.owner,
                isInvalid ? PCLGameUtilities.GetRandomEnemy(true) : target,
                isInvalid ? info.base : info.output);
        this.applyPowers = card != null && isInvalid;
    }

    public DealDamage ApplyPowers(boolean applyPowers)
    {
        this.applyPowers = applyPowers;
        return this;
    }

    public DealDamage SetDamageEffect(FuncT1<Float, AbstractCreature> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamage SetPiercing(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamage SetVFXColor(Color color)
    {
        this.vfxColor = color.cpy();

        return this;
    }

    public DealDamage SetVFXColor(Color color, Color enemyTint)
    {
        this.vfxColor = color.cpy();
        this.enemyTint = enemyTint.cpy();

        return this;
    }

    public DealDamage SetSoundPitch(float pitchMin, float pitchMax)
    {
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;

        return this;
    }

    public DealDamage SetVFX(boolean superFast, boolean muteSfx)
    {
        this.skipWait = superFast;

        if (muteSfx)
        {
            this.pitchMin = this.pitchMax = 0;
        }

        return this;
    }

    public DealDamage SetPCLAttackType(PCLAttackType pclAttackType)
    {
        this.pclAttackType = pclAttackType;

        return this;
    }
    public DealDamage SetPCLAttackType(PCLAttackType pclAttackType, boolean applyPowerRemovalMultiplier)
    {
        this.pclAttackType = pclAttackType;
        this.applyPowerRemovalMultiplier = applyPowerRemovalMultiplier;

        return this;
    }

    public DealDamage SetOptions(boolean superFast, boolean isOrb, boolean canKill)
    {
        this.skipWait = superFast;
        this.isOrb = isOrb;
        this.canKill = canKill;
        return this;
    }

    public DealDamage CanKill(boolean value)
    {
        this.canKill = value;

        return this;
    }

    public DealDamage StealGold(int goldAmount)
    {
        this.goldAmount = goldAmount;

        return this;
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || (this.source != null && this.source.isDying) || (this.info.owner != null && (this.info.owner.isDying || this.info.owner.halfDead));
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && this.shouldCancelAction())
        {
            Complete();
            return;
        }

        if (PCLGameUtilities.IsDeadOrEscaped(target))
        {
            if (PCLGameUtilities.GetEnemies(true).size() > 0)
            {
                PCLActions.Top.Add(new DealDamage(PCLGameUtilities.GetRandomEnemy(true), this));
            }

            Complete();
            return;
        }

        if (onDamageEffect != null)
        {
            AddDuration(onDamageEffect.Invoke(target));
        }

        if (this.goldAmount > 0)
        {
            PCLActions.Instant.StackPower(source, new StolenGoldPower(target, goldAmount));
        }

        if (pclAttackType.powerToRemove != null && target.hasPower(pclAttackType.powerToRemove)) {
            if (applyPowerRemovalMultiplier) {
                info.output *= pclAttackType.GetDamageMultiplier();
            }
            PCLActions.Last.AddPowerEffectEnemyBonus(pclAttackType.powerToRemove, pclAttackType.reactionIncrease);
            PCLActions.Last.ReducePower(source, target, pclAttackType.powerToRemove, 1);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (this.info.type != DamageInfo.DamageType.THORNS && shouldCancelAction())
        {
            Complete();
            return;
        }

        if (!hasPlayedEffect && duration <= 0.1f)
        {
            AddDuration(AttackEffects.GetDamageDelay(attackEffect));
            PCLGameEffects.List.Attack(source, target, attackEffect, pitchMin, pitchMax, vfxColor);
            hasPlayedEffect = true;
        }

        if (TickDuration(deltaTime))
        {
            if (applyPowers)
            {
                if (card != null)
                {
                    card.calculateCardDamage((AbstractMonster) target);
                    this.info.output = card.damage;
                }
                else
                {
                    this.info.applyPowers(this.info.owner, target);
                }
            }

            if (isOrb)
            {
                this.info.output = AbstractOrb.applyLockOn(target, this.info.output);
            }

            if (!canKill)
            {
                info.output = Math.max(0, Math.min(PCLGameUtilities.GetHP(target, true, true) - 1, info.output));
            }
            DamageHelper.ApplyTint(target, enemyTint, attackEffect);
            DamageHelper.DealDamage(target, info, bypassBlock, bypassThorns);

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            {
                PCLGameUtilities.ClearPostCombatActions();
            }

            if (!this.skipWait && !Settings.FAST_MODE)
            {
                PCLActions.Top.Wait(0.1f);
            }

            Complete(target);
        }
    }
}
