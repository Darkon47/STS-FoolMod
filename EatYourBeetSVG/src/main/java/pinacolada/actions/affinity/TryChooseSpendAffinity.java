package pinacolada.actions.affinity;

import pinacolada.cards.base.AffinityChoice;
import pinacolada.cards.base.AffinityChoiceBuilder;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.misc.GenericEffects.GenericEffect_PayAffinity;

public class TryChooseSpendAffinity extends TryChooseAffinity
{
    public TryChooseSpendAffinity(String sourceName, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, PCLAffinity.Extended());
    }

    public TryChooseSpendAffinity(String sourceName, int cost, PCLAffinity... affinities)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, 1, cost, affinities);
    }

    public TryChooseSpendAffinity(String sourceName, int amount, int cost)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, cost, PCLAffinity.Extended());
    }

    public TryChooseSpendAffinity(ActionType type, String sourceName, int amount, int cost, PCLAffinity... affinities)
    {
        super(type, sourceName, amount, cost, affinities);
    }

    @Override
    protected AffinityChoice GetCard(PCLAffinity affinity) {
        int req = sourceAffinities != null ? sourceAffinities.GetRequirement(sourceAffinities.GetRequirement(PCLAffinity.General) > 0 ? PCLAffinity.General : affinity) : cost >= 0 ? cost : System.GetAffinityLevel(affinity, true);
        if (System.GetAffinityLevel(affinity,true) >= req) {
            GenericEffect_PayAffinity affinityCost = new GenericEffect_PayAffinity(affinity, req);
            AffinityChoiceBuilder builder = new AffinityChoiceBuilder(affinity, cost, affinityCost.GetText()).SetOnUse(affinityCost::Use);
            return builder.Build();
        }
        return null;
    }
}
