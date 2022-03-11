package pinacolada.cards.base.baseeffects.conditions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;

public class BaseCondition_Starter extends BaseCondition {

    public static final String ID = Register(BaseCondition_Starter.class);

    public BaseCondition_Starter()
    {
        super(ID, null, PCLCardTarget.None, 0);
    }

    public BaseCondition_Starter(String[] content) {
        super(content);
    }

    public BaseCondition_Starter(BaseEffect effect) {
        this();
        SetChildEffect(effect);
    }

    public BaseCondition_Starter(BaseEffect... effect) {
        this();
        SetChildEffect(effect);
    }

    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        return info.IsStarter;
    }

    @Override
    public String GetConditionText() {
        return PGR.Tooltips.Starter.title;
    }
}
