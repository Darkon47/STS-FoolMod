package pinacolada.cards.base.cardeffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.*;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public abstract class GenericCardEffect
{
    public enum CardValueSource {
        Damage,
        Block,
        MagicNumber,
        SecondaryNumber,
        HitCount,
        XValue,
    }

    protected static final String SEPARATOR = "|";
    protected static final String SUB_SEPARATOR = ";";

    private static final HashMap<String, Class<? extends GenericCardEffect>> EFFECT_MAP = new HashMap<>();

    public static String Register(Class<? extends GenericCardEffect> type) {
        String id = PGR.PCL.CreateID(type.getSimpleName());
        EFFECT_MAP.put(id, type);
        return id;
    }

    public static GenericCardEffect Get(String serializedString) {
        String[] content = PCLJUtils.SplitString(SEPARATOR, serializedString);
        Constructor<? extends GenericCardEffect> c = PCLJUtils.TryGetConstructor(EFFECT_MAP.get(content[0]), String[].class);
        if (c != null) {
            try {
                return c.newInstance((Object) content);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> String JoinEntityIDs(T[] items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static <T> String JoinEntityIDs(Collection<T> items, FuncT1<String, T> stringFunction) {return PCLJUtils.JoinStrings(SUB_SEPARATOR,PCLJUtils.Map(items, stringFunction));}
    public static String JoinEffectTexts(Collection<GenericCardEffect> effects) {
        return JoinEffectTexts(effects, " NL ");
    }
    public static String JoinEffectTexts(Collection<GenericCardEffect> effects, String delimiter) {
        return PCLJUtils.JoinStrings(" NL ", PCLJUtils.Filter(PCLJUtils.Map(effects, GenericCardEffect::GetText), Objects::nonNull));
    }

    public static GenericCardEffect Apply(PCLCardTarget target, int amount, PCLPowerHelper... powers) {
        return new GenericCardEffect_StackPower(target, amount, powers);
    }

    public static GenericCardEffect Apply(PCLCardTarget target, PCLCard card, CardValueSource valueSource, PCLPowerHelper... powers) {
        return new GenericCardEffect_StackPower(target, 0, powers)
                .SetSourceCard(card, valueSource)
                .SetAmountFromCard();
    }

    public static GenericCardEffect ApplyToSingle(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Normal, amount, powers);
    }

    public static GenericCardEffect ApplyToSingle(PCLCard card, CardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Normal, card, valueSource, powers);
    }

    public static GenericCardEffect ApplyToEnemies(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.AoE, amount, powers);
    }

    public static GenericCardEffect ApplyToEnemies(PCLCard card, CardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.AoE, card, valueSource, powers);
    }

    public static GenericCardEffect ApplyToRandom(int amount, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Random, amount, powers);
    }

    public static GenericCardEffect ApplyToRandom(PCLCard card, CardValueSource valueSource, PCLPowerHelper... powers) {
        return Apply(PCLCardTarget.Random, card, valueSource, powers);
    }

    public static GenericCardEffect ChannelOrb(int amount, PCLOrbHelper orb) {
        return new GenericCardEffect_ChannelOrb(amount, orb);
    }

    public static GenericCardEffect ChannelOrb(PCLCard card, CardValueSource valueSource, PCLOrbHelper orb) {
        return new GenericCardEffect_ChannelOrb(0, orb)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect DealDamage(int amount) {
        return DealDamage(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericCardEffect DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericCardEffect_DealDamage(amount, attackEffect);
    }

    public static GenericCardEffect DealDamage(PCLCard card) {
        return DealDamage(card, CardValueSource.Damage, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericCardEffect DealDamage(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return DealDamage(card, CardValueSource.Damage, attackEffect);
    }

    public static GenericCardEffect DealDamage(PCLCard card, CardValueSource valueSource, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericCardEffect_DealDamage(0, attackEffect)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect DealDamageToAll(int amount) {
        return DealDamageToAll(amount, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericCardEffect DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericCardEffect_DealDamageToAll(amount, attackEffect);
    }

    public static GenericCardEffect DealDamageToAll(PCLCard card) {
        return DealDamageToAll(card, CardValueSource.Damage, AbstractGameAction.AttackEffect.NONE);
    }

    public static GenericCardEffect DealDamageToAll(PCLCard card, AbstractGameAction.AttackEffect attackEffect) {
        return DealDamageToAll(card, CardValueSource.Damage, attackEffect);
    }

    public static GenericCardEffect DealDamageToAll(PCLCard card, CardValueSource valueSource, AbstractGameAction.AttackEffect attackEffect) {
        return new GenericCardEffect_DealDamageToAll(0, attackEffect)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect Draw(int amount) {
        return new GenericCardEffect_Draw(amount);
    }

    public static GenericCardEffect Draw(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_Draw(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect DrawLessNextTurn(int amount) {
        return new GenericCardEffect_NextTurnDrawLess(amount);
    }

    public static GenericCardEffect DrawLessNextTurn(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_NextTurnDrawLess(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect DrawNextTurn(int amount) {
        return new GenericCardEffect_NextTurnDraw(amount);
    }

    public static GenericCardEffect DrawNextTurn(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_NextTurnDraw(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect EnterStance(PCLStanceHelper helper) {
        return new GenericCardEffect_EnterStance(helper);
    }

    public static GenericCardEffect Gain(int amount, PCLPowerHelper... powers) {
        return new GenericCardEffect_StackPower(PCLCardTarget.Self, amount, powers);
    }

    public static GenericCardEffect Gain(PCLCard card, CardValueSource valueSource, PCLPowerHelper... powers) {
        return new GenericCardEffect_StackPower(PCLCardTarget.Self, 0, powers)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect GainAffinity(int amount, PCLAffinity... affinities) {
        return new GenericCardEffect_GainAffinity(amount, affinities);
    }

    public static GenericCardEffect GainAffinity(PCLCard card, CardValueSource valueSource, PCLAffinity... affinities) {
        return new GenericCardEffect_GainAffinity(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect GainAffinityPower(int amount, PCLAffinity... affinities) {
        return new GenericCardEffect_GainAffinityPower(amount, affinities);
    }

    public static GenericCardEffect GainAffinityPower(PCLCard card, CardValueSource valueSource, PCLAffinity... affinities) {
        return new GenericCardEffect_GainAffinityPower(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect GainBlock(int amount) {
        return new GenericCardEffect_GainBlock(amount);
    }

    public static GenericCardEffect GainBlock(PCLCard card) {
        return GainBlock(card, CardValueSource.Block);
    }

    public static GenericCardEffect GainBlock(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_GainBlock(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect GainOrbSlots(int amount) {
        return new GenericCardEffect_GainOrbSlots(amount);
    }

    public static GenericCardEffect GainOrbSlots(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_GainOrbSlots(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect GainTempHP(int amount) {
        return new GenericCardEffect_GainTempHP(amount);
    }

    public static GenericCardEffect GainTempHP(PCLCard card) {
        return GainTempHP(card, CardValueSource.MagicNumber);
    }

    public static GenericCardEffect GainTempHP(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_GainTempHP(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect Obtain(PCLCardData... cardData) {
        return new GenericCardEffect_Obtain(1, 1, cardData);
    }

    public static GenericCardEffect Obtain(int copies, int upgradeTimes, PCLCardData... cardData) {
        return new GenericCardEffect_Obtain(copies, upgradeTimes, cardData);
    }

    public static GenericCardEffect PayAffinity(int amount, PCLAffinity... affinities) {
        return new GenericCardEffect_PayAffinity(amount, affinities);
    }

    public static GenericCardEffect PayAffinity(PCLCard card, CardValueSource valueSource, PCLAffinity... affinities) {
        return new GenericCardEffect_PayAffinity(0, affinities)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect Scry(int amount) {
        return new GenericCardEffect_Scry(amount);
    }

    public static GenericCardEffect Scry(PCLCard card, CardValueSource valueSource) {
        return new GenericCardEffect_Scry(0)
                .SetSourceCard(card, valueSource);
    }

    public static GenericCardEffect TriggerOrb(int amount, PCLOrbHelper orb) {
        return new GenericCardEffect_TriggerOrb(amount, orb);
    }

    public static GenericCardEffect TriggerOrb(PCLCard card, CardValueSource valueSource, PCLOrbHelper orb) {
        return new GenericCardEffect_TriggerOrb(0, orb)
                .SetSourceCard(card, valueSource);
    }

    public String effectID;
    public String entityID;
    public PCLCardTarget target;
    public PCLCard sourceCard;
    public CardValueSource valueSource;
    public int amount;
    public int misc;

    public GenericCardEffect() {
        target = PCLCardTarget.None;
    }

    public GenericCardEffect(String[] content) {
        this.effectID = content[0];
        this.entityID = content[1];
        this.target = PCLCardTarget.valueOf(content[2]);
        this.amount = Integer.parseInt(content[3]);
        this.misc = Integer.parseInt(content[4]);
    }

    public GenericCardEffect(String effectID, String entityID, PCLCardTarget target, int amount) {
        this(effectID, entityID, target, amount, 0);
    }

    public GenericCardEffect(String effectID, String entityID, PCLCardTarget target, int amount, int misc) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.target = target;
        this.amount = amount;
        this.misc = misc;
    }

    public GenericCardEffect SetSourceCard(PCLCard card, CardValueSource valueSource) {
        this.sourceCard = card;
        this.valueSource = valueSource;
        SetAmountFromCard();
        return this;
    }

    public GenericCardEffect SetAmountFromCard() {
        this.amount = GetAmountFromCard();
        return this;
    }

    public final int GetAmountFromCard() {
        if (this.sourceCard != null && this.valueSource != null) {
            switch (valueSource) {
                case Block: return sourceCard.block;
                case Damage: return sourceCard.damage;
                case HitCount: return sourceCard.hitCount;
                case MagicNumber: return sourceCard.magicNumber;
                case SecondaryNumber: return sourceCard.secondaryValue;
                case XValue: return sourceCard.GetXValue();
            }
        }
        return amount;
    }
    public final String[] SplitEntityIDs() {
        return PCLJUtils.SplitString(SUB_SEPARATOR, entityID);
    }
    public final String Serialize() {
        return PCLJUtils.JoinStrings(SEPARATOR, new String[] {
           effectID,
           entityID,
           target.name(),
           String.valueOf(amount),
           String.valueOf(misc)
        });
    }

    public abstract String GetText();
    public abstract void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info);
}
