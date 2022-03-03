package pinacolada.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.markers.TooltipObject;
import pinacolada.powers.common.EnergizedPower;
import pinacolada.powers.common.*;
import pinacolada.powers.fool.MagusFormPower;
import pinacolada.powers.fool.SelfImmolationPower;
import pinacolada.powers.fool.StonedPower;
import pinacolada.powers.fool.SwirledPower;
import pinacolada.powers.replacement.*;
import pinacolada.powers.temporary.*;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PCLPowerHelper extends eatyourbeets.powers.PowerHelper implements TooltipObject
{

    public enum Behavior {
        Permanent,
        SingleTurn,
        Temporary,
        TurnBased,
    }

    protected static final Map<String, PCLPowerHelper> ALL = new HashMap<>();

    public static final PCLPowerHelper Blinded = new PCLPowerHelper(BlindedPower.POWER_ID, PGR.Tooltips.Blinded, BlindedPower::new, Behavior.TurnBased, true, true);
    public static final PCLPowerHelper Burning = new PCLPowerHelper(BurningPower.POWER_ID, PGR.Tooltips.Burning, BurningPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Constricted = new PCLPowerHelper(ConstrictedPower.POWER_ID, PGR.Tooltips.Constricted, PCLConstrictedPower::new, Behavior.Permanent,true, true);
    public static final PCLPowerHelper Electrified = new PCLPowerHelper(ElectrifiedPower.POWER_ID, PGR.Tooltips.Electrified, ElectrifiedPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Freezing = new PCLPowerHelper(FreezingPower.POWER_ID, PGR.Tooltips.Freezing, FreezingPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper LockOn = new PCLPowerHelper(LockOnPower.POWER_ID, PGR.Tooltips.LockOn, PCLLockOnPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Poison = new PCLPowerHelper(PoisonPower.POWER_ID, PGR.Tooltips.Poison, PoisonPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Rippled = new PCLPowerHelper(RippledPower.POWER_ID, PGR.Tooltips.Rippled, RippledPower::new, Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Shackles = new PCLPowerHelper(ShacklesPower.POWER_ID, PGR.Tooltips.Shackles, ShacklesPower::new, Behavior.SingleTurn,true, true);
    public static final PCLPowerHelper Weak = new PCLPowerHelper(WeakPower.POWER_ID, PGR.Tooltips.Weak, (o, s, a) -> new PCLWeakPower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Vulnerable = new PCLPowerHelper(VulnerablePower.POWER_ID, PGR.Tooltips.Vulnerable, (o, s, a) -> new PCLVulnerablePower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true, true);
    public static final PCLPowerHelper Frail = new PCLPowerHelper(FrailPower.POWER_ID, PGR.Tooltips.Frail, (o, s, a) -> new PCLFrailPower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,true, true);
    public static final PCLPowerHelper DelayedDamage = new PCLPowerHelper(DelayedDamagePower.POWER_ID, PGR.Tooltips.DelayedDamage, (o, s, a) -> new DelayedDamagePower(o, a, AttackEffects.CLAW), Behavior.SingleTurn,true, true);
    public static final PCLPowerHelper NextTurnDrawLess = new PCLPowerHelper(TemporaryDrawReductionPower.POWER_ID, PGR.Tooltips.NextTurnDrawLess, (o, s, a) -> new TemporaryDrawReductionPower(o, a, true), Behavior.SingleTurn,false, true);
    public static final PCLPowerHelper SelfImmolation = new PCLPowerHelper(SelfImmolationPower.POWER_ID, PGR.Tooltips.SelfImmolation, (o, s, a) -> new SelfImmolationPower(o, a, PCLGameUtilities.IsMonster(s)), Behavior.TurnBased,false, true);
    public static final PCLPowerHelper Slow = new PCLPowerHelper(AntiArtifactSlowPower.POWER_ID, PGR.Tooltips.Slow, AntiArtifactSlowPower::new, Behavior.Permanent,false, true);
    public static final PCLPowerHelper Stoned = new PCLPowerHelper(StonedPower.POWER_ID, PGR.Tooltips.Stoned, SwirledPower::new, Behavior.Permanent,false, true);
    public static final PCLPowerHelper Swirled = new PCLPowerHelper(SwirledPower.POWER_ID, PGR.Tooltips.Swirled, SwirledPower::new, Behavior.Permanent,false, true);

    public static final PCLPowerHelper Artifact = new PCLPowerHelper(ArtifactPower.POWER_ID, PGR.Tooltips.Artifact, ArtifactPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Blur = new PCLPowerHelper(BlurPower.POWER_ID, PGR.Tooltips.Blur, BlurPower::new, Behavior.TurnBased,true, false);
    public static final PCLPowerHelper CounterAttack = new PCLPowerHelper(CounterAttackPower.POWER_ID, PGR.Tooltips.CounterAttack, CounterAttackPower::new, Behavior.SingleTurn,true, false);
    public static final PCLPowerHelper Dexterity = new PCLPowerHelper(DexterityPower.POWER_ID, PGR.Tooltips.Dexterity, DexterityPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Focus = new PCLPowerHelper(FocusPower.POWER_ID, PGR.Tooltips.Focus, FocusPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Inspiration = new PCLPowerHelper(InspirationPower.POWER_ID, PGR.Tooltips.Inspiration, InspirationPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Malleable = new PCLPowerHelper(MalleablePower.POWER_ID, PGR.Tooltips.Malleable, MalleablePower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Metallicize = new PCLPowerHelper(MetallicizePower.POWER_ID, PGR.Tooltips.Metallicize, MetallicizePower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper PlatedArmor = new PCLPowerHelper(PlatedArmorPower.POWER_ID, PGR.Tooltips.PlatedArmor, PlatedArmorPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Resistance = new PCLPowerHelper(ResistancePower.POWER_ID, PGR.Tooltips.Resistance, ResistancePower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Sorcery = new PCLPowerHelper(SorceryPower.POWER_ID, PGR.Tooltips.Sorcery, SorceryPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Strength = new PCLPowerHelper(StrengthPower.POWER_ID, PGR.Tooltips.Strength, StrengthPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper SupportDamage = new PCLPowerHelper(SupportDamagePower.POWER_ID, PGR.Tooltips.SupportDamage, SupportDamagePower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Thorns = new PCLPowerHelper(ThornsPower.POWER_ID, PGR.Tooltips.Thorns, ThornsPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Vitality = new PCLPowerHelper(VitalityPower.POWER_ID, PGR.Tooltips.Vitality, VitalityPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper Vigor = new PCLPowerHelper(VigorPower.POWER_ID, PGR.Tooltips.Vigor, VigorPower::new, Behavior.Permanent,true, false);
    public static final PCLPowerHelper BurningWeapon = new PCLPowerHelper(BurningWeaponPower.POWER_ID, PGR.Tooltips.BurningWeapon, (o, s, a) -> new BurningWeaponPower(o, a), Behavior.TurnBased,false, false);
    public static final PCLPowerHelper CurlUp = new PCLPowerHelper(PCLCurlUpPower.POWER_ID, PGR.Tooltips.CurlUp, PCLCurlUpPower::new, Behavior.Permanent,false, false);
    public static final PCLPowerHelper DemonForm = new PCLPowerHelper(DemonFormPower.POWER_ID, PGR.Tooltips.DemonForm, DemonFormPower::new, Behavior.Permanent,false, false);
    public static final PCLPowerHelper EnchantedArmor = new PCLPowerHelper(EnchantedArmorPower.POWER_ID, PGR.Tooltips.EnchantedArmor, (o, s, a) -> new EnchantedArmorPower(o, a), Behavior.Permanent,false, false);
    public static final PCLPowerHelper Energized = new PCLPowerHelper(EnergizedPower.POWER_ID, PGR.Tooltips.Energized, VigorPower::new, Behavior.SingleTurn,false, false);
    public static final PCLPowerHelper Enrage = new PCLPowerHelper(EnragePower.POWER_ID, PGR.Tooltips.Enrage, EnragePower::new, Behavior.SingleTurn,false, false);
    public static final PCLPowerHelper Flight = new PCLPowerHelper(PlayerFlightPower.POWER_ID, PGR.Tooltips.Flight, PlayerFlightPower::new, Behavior.Permanent,false, false);
    public static final PCLPowerHelper Intangible = new PCLPowerHelper(IntangiblePlayerPower.POWER_ID, PGR.Tooltips.Intangible, IntangiblePlayerPower::new, Behavior.TurnBased,false, false);
    public static final PCLPowerHelper MagusForm = new PCLPowerHelper(MagusFormPower.POWER_ID, PGR.Tooltips.MagusForm, MagusFormPower::new, Behavior.Permanent,false, false);
    public static final PCLPowerHelper NextTurnBlock = new PCLPowerHelper(NextTurnBlockPower.POWER_ID, PGR.Tooltips.NextTurnBlock, (o, s, a) -> new NextTurnBlockPower(o, a), Behavior.SingleTurn,false, false);
    public static final PCLPowerHelper NextTurnDraw = new PCLPowerHelper(DrawCardNextTurnPower.POWER_ID, PGR.Tooltips.NextTurnDraw, DrawCardNextTurnPower::new, Behavior.SingleTurn,false, false);
    public static final PCLPowerHelper Regen = new PCLPowerHelper(RegenPower.POWER_ID, PGR.Tooltips.Regeneration, RegenPower::new, Behavior.TurnBased,false, false);
    public static final PCLPowerHelper Ritual = new PCLPowerHelper(RitualPower.POWER_ID, PGR.Tooltips.Ritual, (o, s, a) -> new RitualPower(o, a, PCLGameUtilities.IsPlayer(o)), Behavior.Permanent,false, false);
    public static final PCLPowerHelper Supercharged = new PCLPowerHelper(SuperchargedPower.POWER_ID, PGR.Tooltips.Supercharged, SuperchargedPower::new, Behavior.Permanent,false, false);
    public static final PCLPowerHelper TemporaryArtifact = new PCLPowerHelper(TemporaryArtifactPower.POWER_ID, PGR.Tooltips.Artifact, TemporaryArtifactPower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryDexterity = new PCLPowerHelper(TemporaryDexterityPower.POWER_ID, PGR.Tooltips.Dexterity, TemporaryDexterityPower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryFocus = new PCLPowerHelper(TemporaryFocusPower.POWER_ID, PGR.Tooltips.Focus, TemporaryFocusPower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryMalleable = new PCLPowerHelper(TemporaryMalleablePower.POWER_ID, PGR.Tooltips.Malleable, TemporaryMalleablePower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryResistance = new PCLPowerHelper(TemporaryResistancePower.POWER_ID, PGR.Tooltips.Resistance, TemporaryResistancePower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryStrength = new PCLPowerHelper(TemporaryStrengthPower.POWER_ID, PGR.Tooltips.Strength, TemporaryStrengthPower::new, Behavior.Temporary,false, false);
    public static final PCLPowerHelper TemporaryThorns = new PCLPowerHelper(TemporaryThornsPower.POWER_ID, PGR.Tooltips.Thorns, TemporaryThornsPower::new, Behavior.Temporary,false, false);

    public PCLCardTooltip Tooltip;
    public final String ID;
    public final Behavior EndTurnBehavior;
    public final boolean IsCommon;
    public final boolean IsDebuff;
    protected final FuncT2<AbstractPower, AbstractCreature, Integer> constructorT2;
    protected final FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructorT3;

    public static PCLPowerHelper Get(String powerID) {
        return ALL.get(powerID);
    }

    public static Collection<PCLPowerHelper> Values() {
        return ALL.values();
    }

    public PCLPowerHelper(String powerID, PCLCardTooltip tooltip, FuncT2<AbstractPower, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isCommon, boolean isDebuff)
    {
        super(powerID, tooltip, constructor);
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = constructor;
        this.constructorT3 = null;
        this.EndTurnBehavior = endTurnBehavior;
        this.IsCommon = isCommon;
        this.IsDebuff = isDebuff;
    }

    public PCLPowerHelper(String powerID, PCLCardTooltip tooltip, FuncT3<AbstractPower, AbstractCreature, AbstractCreature, Integer> constructor, Behavior endTurnBehavior, boolean isCommon, boolean isDebuff)
    {
        super(powerID, tooltip, constructor);
        ALL.putIfAbsent(powerID, this);

        this.ID = powerID;
        this.Tooltip = tooltip;
        this.constructorT2 = null;
        this.constructorT3 = constructor;
        this.EndTurnBehavior = endTurnBehavior;
        this.IsCommon = isCommon;
        this.IsDebuff = isDebuff;
    }

    public AbstractPower Create(AbstractCreature owner, AbstractCreature source, int amount)
    {
        if (constructorT2 != null)
        {
            return constructorT2.Invoke(owner, amount);
        }
        else if (constructorT3 != null)
        {
            return constructorT3.Invoke(owner, source, amount);
        }
        else
        {
            throw new RuntimeException("Do not create a PowerHelper with a null constructor.");
        }
    }

    @Override
    public PCLCardTooltip GetTooltip() {
        return Tooltip;
    }
}
