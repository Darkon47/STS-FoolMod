package pinacolada.cards.base.baseeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class CompositeEffect extends BaseEffect {
    protected final ArrayList<BaseEffect> effects;

    public static final String ID = Register(CompositeEffect.class, 0);

    public CompositeEffect(BaseEffect... effects) {
        super(ID);
        this.target = PCLJUtils.Max(effects, effect -> effect.target);
        this.effects = new ArrayList<>(Arrays.asList(effects));
        this.entityID = BaseEffect.JoinEntityIDs(effects, effect -> effect.entityID);
    }

    public CompositeEffect AddEffect(BaseEffect newEffect) {
        this.effects.add(newEffect);
        this.entityID = BaseEffect.JoinEntityIDs(effects, effect -> effect.entityID);
        return this;
    }

    public CompositeEffect SetEffects(BaseEffect... effects) {
        this.effects.clear();
        this.effects.addAll(Arrays.asList(effects));
        this.entityID = BaseEffect.JoinEntityIDs(effects, effect -> effect.entityID);
        return this;
    }

    @Override
    public AbstractAttribute GetDamageInfo() {
        for (BaseEffect be : effects) {
            AbstractAttribute e = be.GetDamageInfo();
            if (e != null) {
                return e;
            }
        }
        return super.GetDamageInfo();
    }

    @Override
    public AbstractAttribute GetBlockInfo() {
        for (BaseEffect be : effects) {
            AbstractAttribute e = be.GetBlockInfo();
            if (e != null) {
                return e;
            }
        }
        return super.GetBlockInfo();
    }

    @Override
    public AbstractAttribute GetSpecialInfo() {
        for (BaseEffect be : effects) {
            AbstractAttribute e = be.GetSpecialInfo();
            if (e != null) {
                return e;
            }
        }
        return super.GetSpecialInfo();
    }

    @Override
    public BaseEffect SetAmountFromCard() {
        super.SetAmountFromCard();
        for (BaseEffect effect : effects) {
            effect.SetAmountFromCard();
        }
        return this;
    }

    @Override
    public String GetText() {
        return BaseEffect.JoinEffectTexts(effects);
    }

    @Override
    public String GetSampleText() {
        return null;
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        for (BaseEffect effect : effects) {
            effect.Use(p, m, info);
        }
    }
}
