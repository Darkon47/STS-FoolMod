package pinacolada.cards.base.baseeffects.conditions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;

public class BaseCondition_SemiLimited extends BaseCondition {

    public static final String ID = Register(BaseCondition_SemiLimited.class, 1);

    public BaseCondition_SemiLimited()
    {
        super(ID, null, PCLCardTarget.None, 0);
    }

    public BaseCondition_SemiLimited(String[] content) {
        super(content);
    }

    public BaseCondition_SemiLimited(BaseEffect effect) {
        this();
        SetChildEffect(effect);
    }

    public BaseCondition_SemiLimited(BaseEffect... effect) {
        this();
        SetChildEffect(effect);
    }

    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        return isUsing ? CheckChild(p, m, info, isUsing) && info.TryActivateSemiLimited() : info.CanActivateSemiLimited;
    }

    @Override
    public String GetConditionText() {
        return PGR.Tooltips.SemiLimited.title;
    }
}
