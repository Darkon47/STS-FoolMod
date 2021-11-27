package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class IncreaseAffinityPowerLevel extends EYBActionWithCallback<AbstractPower>
{
    public AbstractAffinityPower power;
    public boolean showEffect;

    public IncreaseAffinityPowerLevel(AbstractCreature source, Affinity affinity, int amount)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);


        if (affinity != null)
        {
            this.power = CombatStats.Affinities.GetPower(affinity);
        }
        else
        {
            this.power = GameUtilities.GetRandomElement(CombatStats.Affinities.Powers, EYBCard.rng);
        }

        if (power == null || AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            Complete();
            return;
        }

        Initialize(source, power.owner, amount);
    }

    public IncreaseAffinityPowerLevel ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount == 0)
        {
            Complete();
            return;
        }

        if (shouldCancelAction() || !GameUtilities.CanApplyPower(source, target, power, this))
        {
            Complete(power);
            return;
        }

        if (source != null)
        {
            for (AbstractPower power : source.powers)
            {
                power.onApplyPower(this.power, target, source);
            }
        }

        power.IncreaseLevel(amount);
        power.flash();

        if (showEffect)
        {
            GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, "+" + amount + " " + power.name));
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(power);
        }
    }
}