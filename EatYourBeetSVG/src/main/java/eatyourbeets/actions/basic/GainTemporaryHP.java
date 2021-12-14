package eatyourbeets.actions.basic;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameEffects;

public class GainTemporaryHP extends EYBActionWithCallback<AbstractCreature>
{
    public GainTemporaryHP(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(ActionType.HEAL, Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    public GainTemporaryHP(AbstractCreature target, AbstractCreature source, int amount, boolean superFast)
    {
        super(ActionType.HEAL, superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    @Override
    protected void FirstUpdate()
    {

        if (!target.isDying && !target.isDead)
        {
            int gainAmount = CombatStats.OnGainTempHP(amount);
            TempHPField.tempHp.set(target, TempHPField.tempHp.get(target) + gainAmount);
            if (gainAmount > 0)
            {
                GameEffects.Queue.Add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, gainAmount));
                target.healthBarUpdatedEvent();
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(target);
        }
    }
}
