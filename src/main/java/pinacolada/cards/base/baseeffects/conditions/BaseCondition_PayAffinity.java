package pinacolada.cards.base.baseeffects.conditions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseCondition_PayAffinity extends BaseCondition
{
    public static final String ID = Register(BaseCondition_PayAffinity.class);

    protected ArrayList<PCLAffinity> affinities;

    public BaseCondition_PayAffinity(String[] content)
    {
        super(content);
        this.affinities = ParseAffinitiesFromEntityID();
    }

    public BaseCondition_PayAffinity(int amount, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, affinity -> affinity.Name), PCLCardTarget.None, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
    }

    public BaseCondition_PayAffinity Add(PCLAffinity newAf) {
        this.affinities.add(newAf);
        this.entityID = JoinEntityIDs(affinities, af -> af.Name);
        return this;
    }

    @Override
    public String GetConditionText()
    {
        return PGR.PCL.Strings.Actions.Pay(amount, PCLJUtils.JoinStrings(" ", PCLJUtils.Map(affinities, PCLAffinity::GetTooltip)), false);
    }

    @Override
    public int GetCardAffinityValue() {
        if (sourceCard != null) {
            for (PCLAffinity af : affinities) {
                int val = sourceCard.affinities.GetRequirement(af);
                if (val > 0) {
                    return val;
                }
            }
        }
        return super.GetCardAffinityValue();
    }

    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        for (PCLAffinity affinity : affinities) {
            if (PCLCombatStats.MatchingSystem.GetAffinityLevel(affinity, true) < amount) {
                return false;
            }
        }
        if (isUsing) {
            for (PCLAffinity affinity : affinities) {
                if (!PCLGameUtilities.TrySpendAffinity(affinity, amount, true)) {
                    return false;
                }
            }
        }
        return true;
    }
}
