package pinacolada.cards.base;

import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.common.RippledPower;
import pinacolada.powers.fool.StonedPower;
import pinacolada.powers.fool.SwirledPower;

import java.util.Arrays;
import java.util.HashSet;

public enum PCLAttackType
{
    None(false, false, 0),
    Normal(false, false, 0),
    Air(false, true, 0, StonedPower.POWER_ID),
    Brutal(false, false, 0),
    Dark(false, true, 60, ElectrifiedPower.POWER_ID),
    Earth(false, true, 60, SwirledPower.POWER_ID),
    Electric(false, true, 60, RippledPower.POWER_ID),
    Fire(false, true, 60, FreezingPower.POWER_ID),
    Ice(false, true, 60, BurningPower.POWER_ID),
    Piercing(true, true, 0),
    Ranged(false, true, 0);

    public static final int BASE_DAMAGE_MULTIPLIER = 60;
    public final boolean bypassThorns;
    public final boolean bypassBlock;
    public final HashSet<String> reactionPowers;
    public final int damageMultiplier;

    PCLAttackType(boolean bypassBlock, boolean bypassThorns, int damageMultiplier, String... reactionPowers)
    {
        this.bypassThorns = bypassThorns;
        this.bypassBlock = bypassBlock;
        this.reactionPowers = new HashSet<String>(Arrays.asList(reactionPowers));
        this.damageMultiplier = damageMultiplier;
    }

    public float GetDamageMultiplier(String powerID) {
        return 1f + (GetDamageMultiplierForDisplay(powerID) / 100f);
    }

    public int GetDamageMultiplierForDisplay(String powerID) {
        return damageMultiplier + PCLCombatStats.GetAmplifierBonus(powerID);
    }
}
