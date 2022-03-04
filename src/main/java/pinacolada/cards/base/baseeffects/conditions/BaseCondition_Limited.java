package pinacolada.cards.base.baseeffects.conditions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.CardTooltips;

public class BaseCondition_Limited extends BaseCondition {

    public static final String ID = Register(BaseCondition_Limited.class);

    public BaseCondition_Limited()
    {
        super(ID, null, PCLCardTarget.None, 0);
    }

    public BaseCondition_Limited(String[] content) {
        super(content);
    }

    public BaseCondition_Limited(BaseEffect effect) {
        this();
        SetChildEffect(effect);
    }

    public BaseCondition_Limited(BaseEffect... effect) {
        this();
        SetChildEffect(effect);
    }

    @Override
    public boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {
        return isUsing ? info.TryActivateLimited() : info.CanActivateLimited;
    }

    @Override
    public String GetConditionText() {
        return CardTooltips.FindByID("Limited").title;
    }
}
