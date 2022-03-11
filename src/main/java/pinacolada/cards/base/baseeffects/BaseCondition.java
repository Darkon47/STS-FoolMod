package pinacolada.cards.base.baseeffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.conditions.*;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Objects;

public abstract class BaseCondition extends BaseEffect {
    public static final int CONDITION_PRIORITY = 2;

    public static String Register(Class<? extends BaseEffect> type) {
        return Register(type, CONDITION_PRIORITY);
    }

    public static String Register(Class<? extends BaseEffect> type, AbstractCard.CardColor... cardColors) {
        return Register(type, CONDITION_PRIORITY, cardColors);
    }

    public static ArrayList<BaseCondition> GetEligibleConditions(AbstractCard.CardColor co, Integer priority) {
        return PCLJUtils.Filter(PCLJUtils.Map(GetEligibleEffects(co, priority), ef -> PCLJUtils.SafeCast(ef, BaseCondition.class)), Objects::nonNull);
    }

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

    public BaseCondition() {
        super();
    }

    public BaseCondition(SerializedData content) {
        super(content);
        if (misc != null) {
            this.childEffect = BaseEffect.Get(misc);
        }
    }

    public BaseCondition(String effectID) {
        super(effectID, null, PCLCardTarget.None, 0, null);
    }

    public BaseCondition(String effectID, String entityID) {
        super(effectID, entityID, PCLCardTarget.None, 0, null);
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
        this.misc = effect != null ? effect.Serialize() : null;
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
    public String GetSampleText() {
        return GetConditionText();
    }

    @Override
    public String GetText()
    {
        return GetConditionText() + (childEffect != null ? ((childEffect instanceof BaseCondition ? ". " : ": ") + childEffect.GetText()) : "");
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (CheckCondition(p, m, info, true) && childEffect != null) {
            childEffect.Use(p, m, info);
        }
    }

    public final BaseEffect GetChild() {return this.childEffect;}

    public final boolean HasChildCondition() {return this.childEffect instanceof BaseCondition;}

    public final boolean CheckChild(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing) {return !(HasChildCondition()) || ((BaseCondition) this.childEffect).CheckCondition(p, m, info, isUsing);}

    public final boolean HasCreate() {
        return (this instanceof BaseCondition_WhenCreated || (HasChildCondition() && ((BaseCondition) this.childEffect).HasCreate()));
    }

    public final boolean HasDiscard() {
        return (this instanceof BaseCondition_OnDiscard || (HasChildCondition() && ((BaseCondition) this.childEffect).HasDiscard()));
    }

    public final boolean HasDraw() {
        return (this instanceof BaseCondition_WhenDrawn || (HasChildCondition() && ((BaseCondition) this.childEffect).HasDraw()));
    }

    public final boolean HasExhaust() {
        return (this instanceof BaseCondition_OnExhaust || (HasChildCondition() && ((BaseCondition) this.childEffect).HasExhaust()));
    }

    public final boolean HasPurge() {
        return (this instanceof BaseCondition_OnPurge || (HasChildCondition() && ((BaseCondition) this.childEffect).HasPurge()));
    }

    public abstract boolean CheckCondition(AbstractPlayer p, AbstractMonster m, CardUseInfo info, boolean isUsing);
    public abstract String GetConditionText();
}
