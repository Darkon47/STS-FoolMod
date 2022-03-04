package pinacolada.cards.base.baseeffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.baseeffects.effects.*;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public abstract class BaseEffect
{
    public enum PCLCardValueSource {
        Damage,
        Block,
        MagicNumber,
        SecondaryNumber,
        HitCount,
        Affinity,
        XValue,
    }

    protected static final String SEPARATOR = "|";
    protected static final String SUB_SEPARATOR = ";";

    private static final HashMap<String, Class<? extends BaseEffect>> EFFECT_MAP = new HashMap<>();

    public static String Register(Class<? extends BaseEffect> type) {
        String id = PGR.PCL.CreateID(type.getSimpleName());
        EFFECT_MAP.put(id, type);
        return id;
    }

    public static BaseEffect Get(String serializedString) {
        String[] content = PCLJUtils.SplitString(SEPARATOR, serializedString);
        Constructor<? extends BaseEffect> c = PCLJUtils.TryGetConstructor(EFFECT_MAP.get(content[0]), String[].class);
        if (c != null) {
            try {
                return c.newInstance((Object) content);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String GetTargetString(PCLCardTarget target) {
        switch (target) {
            case All:
            case AoE:
                return PGR.PCL.Strings.Actions.ALLEnemies;
            case Random:
                return PGR.PCL.Strings.Actions.RandomEnemy;
            default:
                return PGR.PCL.Strings.Actions.Enemy;
        }
    }
    public static <T> String JoinEntityIDs(T[] items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static <T> String JoinEntityIDs(Collection<T> items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static String JoinEffectTexts(Collection<BaseEffect> effects) {
        return JoinEffectTexts(effects, " NL ");
    }
    public static String JoinEffectTexts(Collection<BaseEffect> effects, String delimiter) {
        return PCLJUtils.JoinStrings(delimiter, PCLJUtils.Filter(PCLJUtils.Map(effects, BaseEffect::GetText), Objects::nonNull));
    }

    public static BaseEffect Apply(PCLCardTarget target, int amount, PCLPowerHelper... powers) {
        return new BaseEffect_StackPower(target, amount, powers);
    }

    public static BaseEffect Apply(PCLCardTarget target, PCLCard card, PCLCardValueSource valueSource, PCLPowerHelper... powers) {
        return new BaseEffect_StackPower(target, 0, powers)
                .SetSourceCard(card, valueSource)
                .SetAmountFromCard();
    }

    public static BaseEffect ApplyToSingle(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Normal, amount, powers);
    }

    public static BaseEffect ApplyToSingle(PCLCard card, PCLCardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Normal, card, valueSource, powers);
    }

    public static BaseEffect ApplyToEnemies(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.AoE, amount, powers);
    }

    public static BaseEffect ApplyToEnemies(PCLCard card, PCLCardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.AoE, card, valueSource, powers);
    }

    public static BaseEffect ApplyToRandom(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Random, amount, powers);
    }

    public static BaseEffect ApplyToRandom(PCLCard card, PCLCardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Random, card, valueSource, powers);
    }

    public static BaseEffect ChannelOrb(int amount, PCLOrbHelper orb) {
        return new BaseEffect_ChannelOrb(amount, orb);
    }

    public static BaseEffect ChannelOrb(PCLCard card, PCLCardValueSource valueSource, PCLOrbHelper orb) {
        return new BaseEffect_ChannelOrb(0, orb)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect DealDamage(int amount) {
        return DealDamage(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamage(amount, attackEffect);
    }

    public static BaseEffect DealDamage(PCLCard card) {
        return DealDamage(card, PCLCardValueSource.Damage, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamage(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return DealDamage(card, PCLCardValueSource.Damage, attackEffect);
    }

    public static BaseEffect DealDamage(PCLCard card, PCLCardValueSource valueSource, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamage(0, attackEffect)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect DealDamageToAll(int amount) {
        return DealDamageToAll(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamageToAll(amount, attackEffect);
    }

    public static BaseEffect DealDamageToAll(PCLCard card) {
        return DealDamageToAll(card, PCLCardValueSource.Damage, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamageToAll(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return DealDamageToAll(card, PCLCardValueSource.Damage, attackEffect);
    }

    public static BaseEffect DealDamageToAll(PCLCard card, PCLCardValueSource valueSource, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamageToAll(0, attackEffect)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect Draw(int amount) {
        return new BaseEffect_Draw(amount);
    }

    public static BaseEffect Draw(PCLCard card, PCLCardValueSource valueSource) {
        return new BaseEffect_Draw(0)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect EnterStance(PCLStanceHelper helper) {
        return new BaseEffect_EnterStance(helper);
    }

    public static BaseEffect Gain(int amount, PCLPowerHelper... powers) {
        return new BaseEffect_StackPower(PCLCardTarget.Self, amount, powers);
    }

    public static BaseEffect Gain(PCLCard card, PCLCardValueSource valueSource, PCLPowerHelper... powers) {
        return new BaseEffect_StackPower(PCLCardTarget.Self, 0, powers)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect GainAffinity(int amount, PCLAffinity... affinities) {
        return new BaseEffect_GainAffinity(amount, affinities);
    }

    public static BaseEffect GainAffinity(PCLCard card, PCLCardValueSource valueSource, PCLAffinity... affinities) {
        return new BaseEffect_GainAffinity(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect GainAffinityPower(int amount, PCLAffinity... affinities) {
        return new BaseEffect_GainAffinityPower(amount, affinities);
    }

    public static BaseEffect GainAffinityPower(PCLCard card, PCLCardValueSource valueSource, PCLAffinity... affinities) {
        return new BaseEffect_GainAffinityPower(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect GainBlock(int amount) {
        return new BaseEffect_GainBlock(amount);
    }

    public static BaseEffect GainBlock(PCLCard card) {
        return GainBlock(card, PCLCardValueSource.Block);
    }

    public static BaseEffect GainBlock(PCLCard card, PCLCardValueSource valueSource) {
        return new BaseEffect_GainBlock(0)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect GainOrbSlots(int amount) {
        return new BaseEffect_GainOrbSlots(amount);
    }

    public static BaseEffect GainOrbSlots(PCLCard card, PCLCardValueSource valueSource) {
        return new BaseEffect_GainOrbSlots(0)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect GainTempHP(int amount) {
        return new BaseEffect_GainTempHP(amount);
    }

    public static BaseEffect GainTempHP(PCLCard card) {
        return GainTempHP(card, PCLCardValueSource.MagicNumber);
    }

    public static BaseEffect GainTempHP(PCLCard card, PCLCardValueSource valueSource) {
        return new BaseEffect_GainTempHP(0)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect Obtain(PCLCardData... cardData) {
        return new BaseEffect_Obtain(1, 1, cardData);
    }

    public static BaseEffect Obtain(int copies, int upgradeTimes, PCLCardData... cardData) {
        return new BaseEffect_Obtain(copies, upgradeTimes, cardData);
    }

    public static BaseEffect PayAffinity(int amount, PCLAffinity... affinities) {
        return new BaseEffect_PayAffinity(amount, affinities);
    }

    public static BaseEffect PayAffinity(PCLCard card, PCLCardValueSource valueSource, PCLAffinity... affinities) {
        return new BaseEffect_PayAffinity(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect Scry(int amount) {
        return new BaseEffect_Scry(amount);
    }

    public static BaseEffect Scry(PCLCard card, PCLCardValueSource valueSource) {
        return new BaseEffect_Scry(0)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect Stun(int amount) {
        return new BaseEffect_Stun(amount);
    }

    public static BaseEffect TriggerOrb(int amount, PCLOrbHelper orb) {
        return new BaseEffect_TriggerOrb(amount, orb);
    }

    public static BaseEffect TriggerOrb(PCLCard card, PCLCardValueSource valueSource, PCLOrbHelper orb) {
        return new BaseEffect_TriggerOrb(0, orb)
                .SetSourceCard(card, valueSource);
    }

    public String effectID;
    public String entityID;
    public PCLCardTarget target;
    public PCLCardValueSource valueSource;
    public PCLCard sourceCard;
    public int amount;
    public String misc;

    public BaseEffect() {
        target = PCLCardTarget.None;
    }

    public BaseEffect(String[] content) {
        this.effectID = content[0];
        this.entityID = content[1];
        this.target = PCLCardTarget.valueOf(content[2]);
        this.valueSource = PCLCardValueSource.valueOf(content[3]);
        this.amount = Integer.parseInt(content[4]);
        this.misc = content[5];
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount) {
        this(effectID, entityID, target, amount, null);
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount, String misc) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.target = target;
        this.amount = amount;
        this.misc = misc;
    }

    public BaseEffect SetSourceCard(PCLCard card) {
        return SetSourceCard(card, valueSource);
    }

    public BaseEffect SetSourceCard(PCLCard card, PCLCardValueSource valueSource) {
        this.sourceCard = card;
        this.valueSource = valueSource;
        SetAmountFromCard();
        return this;
    }

    public BaseEffect SetAmountFromCard() {
        this.amount = GetAmountFromCard();
        return this;
    }

    public AbstractAttribute GetDamageInfo() {return null;}
    public AbstractAttribute GetBlockInfo() {return null;}
    public AbstractAttribute GetSpecialInfo() {return null;}
    public int GetCardAffinityValue() {return 0;}
    public void OnDrag(AbstractMonster m) {}

    public final ArrayList<PCLAffinity> ParseAffinitiesFromEntityID() {
        return PCLJUtils.Filter(PCLJUtils.Map(SplitEntityIDs(), PCLAffinity::valueOf), Objects::nonNull);
    }
    public final int GetAmountFromCard() {
        if (this.sourceCard != null && this.valueSource != null) {
            switch (valueSource) {
                case Block: return sourceCard.block;
                case Damage: return sourceCard.damage;
                case HitCount: return sourceCard.hitCount;
                case MagicNumber: return sourceCard.magicNumber;
                case SecondaryNumber: return sourceCard.secondaryValue;
                case Affinity: return GetCardAffinityValue();
                case XValue: return sourceCard.GetXValue();
            }
        }
        return amount;
    }
    public final String GetTargetString() {
        return GetTargetString(target);
    }
    public final String[] SplitEntityIDs() {
        return PCLJUtils.SplitString(SUB_SEPARATOR, entityID);
    }
    public final String Serialize() {
        return PCLJUtils.JoinStrings(SEPARATOR, new String[] {
           effectID,
           entityID,
           target != null ? target.name() : PCLCardTarget.None.name(),
           valueSource != null ? valueSource.name() : null,
           String.valueOf(amount),
           misc
        });
    }

    public abstract String GetText();
    public abstract void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info);
}
