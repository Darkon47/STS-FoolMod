package pinacolada.cards.base.baseeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.conditions.*;

public abstract class BaseCondition extends BaseEffect {

    public static BaseCondition Limited() {
        return new BaseCondition_Limited();
    }

    public static BaseCondition Match() {
        return new BaseCondition_Match();
    }

    public static BaseCondition PayAffinity(int amount, PCLAffinity... affinities) {
        return new BaseCondition_PayAffinity(amount, affinities);
    }

    public static BaseCondition PayAffinity(PCLCard card, PCLAffinity... affinities) {
        return new BaseCondition_PayAffinity(0, affinities)
                .SetSourceCard(card, PCLCardValueSource.Affinity);
    }

    public static BaseCondition PayAffinity(PCLCard card, PCLCardValueSource valueSource, PCLAffinity... affinities) {
        return new BaseCondition_PayAffinity(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static BaseCondition SemiLimited() {
        return new BaseCondition_SemiLimited();
    }

    public static BaseCondition Starter() {
        return new BaseCondition_Starter();
    }

    protected BaseEffect childEffect;

    public BaseCondition(String[] content) {
        super(content);
        this.childEffect = BaseEffect.Get(misc);
    }

    public BaseCondition(String effectID, String entityID, PCLCardTarget target, int amount) {
        super(effectID, entityID, target, amount);
    }

    public BaseCondition(String effectID, String entityID, PCLCardTarget target, int amount, BaseEffect effect) {
        super(effectID, entityID, target, amount);
        SetChildEffect(effect);
    }

    public BaseCondition(String effectID, String entityID, PCLCardTarget target, int amount, BaseEffect... effect) {
        super(effectID, entityID, target, amount);
        SetChildEffect(effect);
    }

    public BaseCondition SetChildEffect(BaseEffect effect) {
        this.childEffect = effect;
        this.misc = effect.Serialize();
        return this;
    }

    public BaseCondition SetChildEffect(BaseEffect... effects) {
        this.childEffect = new CompositeEffect(effects);
        this.misc = childEffect.Serialize();
        return this;
    }

    @Override
    public BaseCondition SetSourceCard(PCLCard card) {
        return SetSourceCard(card, valueSource);
    }

    @Override
    public BaseCondition SetSourceCard(PCLCard card, PCLCardValueSource valueSource) {
        super.SetSourceCard(card, valueSource);
        return this;
    }

    @Override
    public String GetText()
    {
        return GetConditionText() + ": " + childEffect.GetText();
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (CheckCondition(p, m, info, true)) {
            childEffect.Use(p, m, info);
        }
    }

    public abstract boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing);
    public abstract String GetConditionText();
}
