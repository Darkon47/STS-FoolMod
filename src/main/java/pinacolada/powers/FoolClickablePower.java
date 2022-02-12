package pinacolada.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.PGR;

public class FoolClickablePower extends PCLClickablePower {

    public static String CreateFullID(Class<? extends PCLPower> type) {
        return PGR.Fool.CreateID(type.getSimpleName());
    }

    public FoolClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount) {
        super(owner, id, type, requiredAmount);
    }

    public FoolClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost) {
        super(owner, id, type, requiredAmount, checkCondition, payCost);
    }

    public FoolClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount) {
        super(owner, cardData, type, requiredAmount);
    }

    public FoolClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost) {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost);
    }

    public FoolClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities) {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost, affinities);
    }

    public FoolClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount) {
        super(owner, relic, type, requiredAmount);
    }

    public FoolClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost) {
        super(owner, relic, type, requiredAmount, checkCondition, payCost);
    }

    public FoolClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities) {
        super(owner, relic, type, requiredAmount, checkCondition, payCost, affinities);
    }
}
