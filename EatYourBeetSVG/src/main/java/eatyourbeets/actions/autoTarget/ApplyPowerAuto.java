package eatyourbeets.actions.autoTarget;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.actions.EYBActionAutoTarget;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class ApplyPowerAuto extends EYBActionAutoTarget<AbstractPower>
{
    public static final String[] TEXT = ApplyPowerAction.TEXT;

    protected PowerHelper powerHelper;

    protected boolean chooseRandomTarget;
    protected boolean ignoreArtifact;
    protected boolean showEffect = true;
    protected boolean skipIfZero;
    protected boolean canStack = true;
    protected boolean faster;

    public ApplyPowerAuto(TargetHelper targetHelper, PowerHelper powerHelper, int amount)
    {
        super(ActionType.POWER);

        this.powerHelper = powerHelper;

        Initialize(targetHelper, amount);

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
        {
            Complete();
        }
    }

    public ApplyPowerAuto CanStack(boolean canStack)
    {
        this.canStack = canStack;

        return this;
    }

    public ApplyPowerAuto SkipIfZero(boolean skipIfZero)
    {
        this.skipIfZero = skipIfZero;

        return this;
    }

    public ApplyPowerAuto IgnoreArtifact(boolean ignoreArtifact)
    {
        this.ignoreArtifact = ignoreArtifact;

        return this;
    }

    public ApplyPowerAuto ShowEffect(boolean showEffect, boolean isFast)
    {
        this.showEffect = showEffect;
        this.faster = isFast;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (AbstractCreature target : FindTargets())
        {
            ApplyPower action = new ApplyPower(source, target, powerHelper.Create(target, source, amount), amount);
            action.IgnoreArtifact(ignoreArtifact);
            action.SetRealtime(isRealtime);
            action.ShowEffect(showEffect, faster);
            action.SkipIfZero(skipIfZero);
            action.CanStack(canStack);
            action.SetCancellable(canCancel);
            for (Object tag : tags)
            {
                action.AddTag(tag);
            }

            GameActions.Top.Add(action).AddCallback((ActionT1<AbstractPower>) this::Complete);
        }

        Complete();
    }
}
