package pinacolada.cards.base.baseeffects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.FieldInfo;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseEffect
{
    private static final Gson GsonReader = new Gson();
    private static final TypeToken<SerializedData> TToken = new TypeToken<SerializedData>(){};
    public static final int DEFAULT_PRIORITY = 3;
    public static final String PREFIX_EFFECTS = "pinacolada.cards.base.baseeffects.";

    public enum PCLCardValueSource {
        None,
        Damage,
        Block,
        MagicNumber,
        SecondaryNumber,
        HitCount,
        Affinity,
        XValue,
    }

    public static class SerializedData {
        public String effectID;
        public String entityID;
        public String target;
        public String valueSource;
        public int amount;
        public int upgrade;
        public String misc;

        public SerializedData(BaseEffect effect) {
            this.effectID = effect.effectID;
            this.entityID = effect.entityID;
            this.target = effect.target.name();
            this.valueSource = effect.valueSource.name();
            this.amount = effect.amount;
            this.upgrade = effect.upgrade;
            this.misc = effect.misc;
        }

        public SerializedData(String effectID, String entityID, String target, String valueSource, int amount, int upgrade, String misc) {
            this.effectID = effectID;
            this.entityID = entityID;
            this.target = target;
            this.valueSource = valueSource;
            this.amount = amount;
            this.upgrade = upgrade;
            this.misc = misc;
        }
    }

    public static class BaseEffectData {
        public final Class<? extends BaseEffect> EffectClass;
        public final Set<AbstractCard.CardColor> Colors;
        public final int Priority;

        public BaseEffectData(Class<? extends BaseEffect> effectClass) {
            this(effectClass, DEFAULT_PRIORITY);
        }

        public BaseEffectData(Class<? extends BaseEffect> effectClass, AbstractCard.CardColor... cardColors) {
            this(effectClass, DEFAULT_PRIORITY, cardColors);
        }

        public BaseEffectData(Class<? extends BaseEffect> effectClass, int priority, AbstractCard.CardColor... cardColors) {
            this.EffectClass = effectClass;
            this.Colors = new HashSet<AbstractCard.CardColor>(Arrays.asList(cardColors));
            this.Priority = priority;
        }

        public final boolean IsColorCompatible(AbstractCard.CardColor co) {
            return Colors.contains(co) || Colors.isEmpty();
        }

        public final boolean MatchesPriority(Integer pr) {
            return pr == null || Priority == pr;
        }
    }

    protected static final String SEPARATOR = "|";
    protected static final String SUB_SEPARATOR = ";";
    protected static final String EMPTY_CHARACTER = "~"; // Null values are serialized as "null", so they won't automatically be turned into nulls if deserialized as strings

    private static final HashMap<String, BaseEffectData> EFFECT_MAP = new HashMap<>();

    // Each ID must be called at least once for it to appear in the card editor
    public static void Initialize() {
        ArrayList<String> effectClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_EFFECTS);
        for (String s : effectClassNames)
        {
            try {
                FieldInfo<String> id = PCLJUtils.GetField("ID", Class.forName(s));
                PCLJUtils.LogInfo(BaseEffect.class, "Adding effect " + id.Get(null));
            }
            catch (Exception ignored) {

            }
        }
    }

    public static String Register(Class<? extends BaseEffect> type) {
        return Register(type, DEFAULT_PRIORITY);
    }

    public static String Register(Class<? extends BaseEffect> type, AbstractCard.CardColor... cardColors) {
        return Register(type, DEFAULT_PRIORITY, cardColors);
    }

    public static String Register(Class<? extends BaseEffect> type, int priority, AbstractCard.CardColor... cardColors) {
        String id = PGR.PCL.CreateID(type.getSimpleName());
        EFFECT_MAP.put(id, new BaseEffectData(type, priority, cardColors));
        return id;
    }

    public static BaseEffect Get(String serializedString) {
        //PCLJUtils.LogInfo(BaseEffect.class, "Attempting" + serializedString);
        try {
            SerializedData data = GsonReader.fromJson(serializedString, TToken.getType());
            Constructor<? extends BaseEffect> c = PCLJUtils.TryGetConstructor(EFFECT_MAP.get(data.effectID).EffectClass, SerializedData.class);
            if (c != null) {
                return c.newInstance(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PCLJUtils.LogError(BaseEffect.class, "Failed to deserialize: " + serializedString);
        }

        return null;
    }

    public static BaseEffectData GetData(String id) {
        return EFFECT_MAP.get(id);
    }

    public static Set<String> GetAllIDs() {
        return EFFECT_MAP.keySet();
    }

    public static Collection<BaseEffectData> GetAllClasses() {
        return EFFECT_MAP.values();
    }

    public static List<BaseEffectData> GetEligibleClasses(AbstractCard.CardColor co, Integer priority) {
        return PCLJUtils.Filter(GetAllClasses(), d -> d.MatchesPriority(priority) && d.IsColorCompatible(co));
    }

    public static List<BaseEffect> GetEligibleEffects(AbstractCard.CardColor co, Integer priority) {
        return PCLJUtils.Filter(PCLJUtils.Map(GetEligibleClasses(co, priority), cl -> {
            //PCLJUtils.LogInfo(null, "Parsing effect " + cl.EffectClass);
            Constructor<? extends BaseEffect> c = PCLJUtils.TryGetConstructor(cl.EffectClass);
            if (c != null) {
                try {
                    return c.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    //PCLJUtils.LogError(BaseEffect.class, "Failed to instantiate: " + cl.EffectClass);
                }
            }
            //PCLJUtils.LogInfo(null, "Failed to parse effect " + cl.EffectClass);
            return null;
        }), Objects::nonNull).stream().sorted((a, b) -> StringUtils.compare(a.GetSampleText(), b.GetSampleText())).collect(Collectors.toList());
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
    public static <T> String JoinEntityIDs(T[] items, FuncT1<String, T> stringFunction) {return items.length > 0 ? PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction)) : null;}
    public static <T> String JoinEntityIDs(Collection<T> items, FuncT1<String, T> stringFunction) {return items.size() > 0 ? PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction)) : null;}
    public static String JoinEffectTexts(Collection<BaseEffect> effects) {
        return JoinEffectTexts(effects, " NL ");
    }
    public static String JoinEffectTexts(Collection<BaseEffect> effects, String delimiter) {
        return PCLJUtils.JoinStrings(delimiter, PCLJUtils.Filter(PCLJUtils.Map(effects, BaseEffect::GetText), Objects::nonNull));
    }
    public static String JoinWithOr(List<String> values) {
        StringJoiner sj = new StringJoiner(", ");
        int var4 = values.size();

        int i = 0;
        for (i = 0; i < values.size() - 1; i++) {
            sj.add(values.get(i));
        }

        return PGR.PCL.Strings.Conditions.Or(sj.toString(), values.get(i), false);
    }
    public static String JoinWithOr(String... values) {
        StringJoiner sj = new StringJoiner(", ");
        int var4 = values.length;

        int i = 0;
        for (i = 0; i < values.length - 1; i++) {
            sj.add(values[i]);
        }

        return PGR.PCL.Strings.Conditions.Or(sj.toString(), values[i], false);
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

    public static BaseEffect DealCardDamage(PCLCard card) {
        return DealCardDamage(card, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealCardDamage(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealCardDamage(card, attackEffect);
    }

    public static BaseEffect DealDamage(int amount) {
        return DealDamage(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamage(amount, attackEffect);
    }

    public static BaseEffect DealDamageToAll(int amount) {
        return DealDamageToAll(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamage(amount, attackEffect, PCLCardTarget.All);
    }

    public static BaseEffect DealDamageToAll(PCLCard card) {
        return DealDamageToAll(card, PCLCardValueSource.Damage, AbstractGameAction.AttackEffect.NONE);
    }

    public static BaseEffect DealDamageToAll(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return DealDamageToAll(card, PCLCardValueSource.Damage, attackEffect);
    }

    public static BaseEffect DealDamageToAll(PCLCard card, PCLCardValueSource valueSource, AbstractGameAction.AttackEffect attackEffect) {
        return new BaseEffect_DealDamage(0, attackEffect, PCLCardTarget.All)
                .SetSourceCard(card, valueSource);
    }

    public static BaseEffect Discard(int amount) {
        return new BaseEffect_Discard(amount, PCLCardGroupHelper.Hand);
    }

    public static BaseEffect Discard(int amount, PCLCardGroupHelper... groups) {
        return new BaseEffect_Discard(amount, groups);
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

    public static BaseEffect Exhaust(int amount) {
        return new BaseEffect_Exhaust(amount, PCLCardGroupHelper.Hand);
    }

    public static BaseEffect Exhaust(int amount, PCLCardGroupHelper... groups) {
        return new BaseEffect_Exhaust(amount, groups);
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

    public static BaseEffect GainCardTempHP(PCLCard card) {
        return new BaseEffect_GainCardTempHP(card);
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
    public PCLCard sourceCard;
    public PCLCardTarget target = PCLCardTarget.None;
    public PCLCardValueSource valueSource = PCLCardValueSource.None;
    public int amount;
    public int upgrade;
    public String misc;

    public BaseEffect() {
    }

    public BaseEffect(SerializedData data) {
        this.effectID = data.effectID;
        this.entityID = data.entityID;
        this.target = PCLCardTarget.valueOf(data.target);
        this.valueSource = PCLCardValueSource.valueOf(data.valueSource);
        this.amount = data.amount;
        this.upgrade = data.upgrade;
        this.misc = data.misc;
    }

    public BaseEffect(String effectID) {
        this(effectID, null, PCLCardTarget.None, 0, null);
    }

    public BaseEffect(String effectID, String entityID) {
        this(effectID, entityID, PCLCardTarget.None, 0, null);
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount) {
        this(effectID, entityID, target, amount, null);
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount, int upgrade) {
        this(effectID, entityID, target, amount, upgrade, null);
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount, String misc) {
        this(effectID, entityID, target, amount, 0, misc);
    }

    public BaseEffect(String effectID, String entityID, PCLCardTarget target, int amount, int upgrade, String misc) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.target = target;
        this.amount = amount;
        this.upgrade = upgrade;
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

    public BaseEffect SetSourceValue(PCLCardValueSource valueSource) {
        this.valueSource = valueSource;
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

    public final boolean IsCompatible(AbstractCard.CardColor co) {
        return EFFECT_MAP.get(effectID).IsColorCompatible(co);
    }
    public final ArrayList<PCLAffinity> ParseAffinitiesFromEntityID() {
        return PCLJUtils.Filter(PCLJUtils.Map(SplitEntityIDs(), PCLAffinity::valueOf), Objects::nonNull);
    }

    public final ArrayList<PCLCardGroupHelper> ParseCardGroupsFromEntityID() {
        return PCLJUtils.Filter(PCLJUtils.Map(SplitEntityIDs(), PCLCardGroupHelper::Get), Objects::nonNull);
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
            return amount + sourceCard.timesUpgraded * upgrade;
        }
        return amount;
    }
    public String GetSampleText() {
        return "";
    }
    public final String GetTargetString() {
        return GetTargetString(target);
    }
    public final BaseEffect SetAmount(int amount) {
        this.amount = amount;
        return this;
    }
    public final BaseEffect SetAmount(int amount, int upgrade) {
        this.amount = amount;
        this.upgrade = upgrade;
        return this;
    }
    public final BaseEffect SetUpgrade(int upgrade) {
        this.upgrade = upgrade;
        return this;
    }
    public final BaseEffect SetTarget(PCLCardTarget target) {
        this.target = target;
        return this;
    }
    public final BaseEffect MakeCopy() {return BaseEffect.Get(Serialize());}
    public final String[] SplitEntityIDs() {
        return PCLJUtils.SplitString(SUB_SEPARATOR, entityID);
    }
    public final String Serialize() {
        return GsonReader.toJson(new SerializedData(this), TToken.getType());
    }

    public abstract String GetText();
    public abstract void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info);
}