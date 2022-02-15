package pinacolada.relics;

import pinacolada.cards.fool.enchantments.Enchantment;
import pinacolada.resources.PGR;

public abstract class FoolEnchantableRelic extends PCLEnchantableRelic {

    public static String CreateFullID(Class<? extends PCLRelic> type)
    {
        return PGR.Fool.CreateID(type.getSimpleName());
    }

    public FoolEnchantableRelic(String id, RelicTier tier, LandingSound sfx, int seriesChoices, boolean allowColorless)
    {
        super(id, tier, sfx, seriesChoices, allowColorless);
    }

    public FoolEnchantableRelic(String id, RelicTier tier, LandingSound sfx, int seriesChoices, boolean allowColorless, Enchantment enchantment)
    {
        super(id, tier, sfx, seriesChoices, allowColorless, enchantment);
    }

}
