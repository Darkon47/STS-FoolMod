package pinacolada.cards.base.baseeffects.conditions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;

public class BaseCondition_OnDiscard extends BaseCondition {

    public static final String ID = Register(BaseCondition_OnDiscard.class);

    public BaseCondition_OnDiscard()
    {
        super(ID, null, PCLCardTarget.None, 0);
    }

    public BaseCondition_OnDiscard(String[] content) {
        super(content);
    }

    public BaseCondition_OnDiscard(BaseEffect effect) {
        this();
        SetChildEffect(effect);
    }

    public BaseCondition_OnDiscard(BaseEffect... effect) {
        this();
        SetChildEffect(effect);
    }

    // This "condition" makes the effect be called in the proper trigger method
    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        return true;
    }

    @Override
    public String GetConditionText() {
        return PGR.PCL.Strings.Conditions.OnDiscard(false);
    }
}
