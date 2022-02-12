package pinacolada.actions.affinity;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.PCLCombatStats;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RerollAffinity extends EYBActionWithCallback<PCLAffinity>
{
    protected PCLAffinityMeter meter;
    protected PCLAffinity affinity;
    protected PCLAffinity[] affinityChoices;
    protected int target;
    public boolean isRandom;
    public boolean showEffect;

    public RerollAffinity(int target)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);

        this.target = target;
        this.isRandom = true;
        this.meter = PCLCombatStats.MatchingSystem.GetActiveMeter();
    }

    public RerollAffinity SetAffinityChoices(PCLAffinity... affinities)
    {
        this.affinityChoices = affinities;

        return this;
    }

    public RerollAffinity SetOptions(boolean isRandom, boolean showEffect)
    {
        this.isRandom = isRandom;
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (isRandom) {
            affinity = meter.Set(PCLGameUtilities.GetRandomElement(GetAffinityChoices()), target);
        }
        else {
            PCLActions.Top.TryChooseAffinity(name, 1, GetAffinityChoices()).AddConditionalCallback(choices -> {
               if (choices.size() > 0) {
                   affinity = meter.Set(choices.get(0).Affinity, target);
               }
            });
        }

        if (showEffect) {
            meter.Flash(target);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(affinity);
        }
    }

    protected PCLAffinity[] GetAffinityChoices() {
        return affinityChoices != null ? affinityChoices : isRandom ?
                PCLJUtils.Filter(PCLAffinity.Basic(), a -> meter.GetCurrentAffinity() != a).toArray(new PCLAffinity[]{})
                : PCLAffinity.Extended();
    }
}
