package pinacolada.cards.base.baseeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class CompositeCondition extends BaseCondition {
    protected final ArrayList<BaseCondition> conditions;

    public static final String ID = Register(CompositeCondition.class, 0);

    public CompositeCondition(BaseCondition... conditions) {
        this.target = PCLJUtils.Max(conditions, effect -> effect.target);
        this.conditions = new ArrayList<>(Arrays.asList(conditions));
        this.entityID = BaseEffect.JoinEntityIDs(conditions, effect -> effect.entityID);
    }

    public CompositeCondition AddCondition(BaseCondition effect) {
        this.conditions.add(effect);
        return this;
    }

    public CompositeCondition SetConditions(BaseCondition... effects) {
        this.conditions.clear();
        this.conditions.addAll(Arrays.asList(effects));
        this.entityID = BaseEffect.JoinEntityIDs(effects, effect -> effect.entityID);
        return this;
    }

    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        return PCLJUtils.Any(conditions, c -> c.CheckCondition(p, m, info, isUsing));
    }

    //TODO
    @Override
    public String GetConditionText() {
        return null;
    }
}
