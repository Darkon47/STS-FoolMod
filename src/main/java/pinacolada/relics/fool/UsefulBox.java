package pinacolada.relics.fool;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.fool.enchantments.Enchantment;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.FoolEnchantableRelic;
import pinacolada.utilities.PCLActions;

public class UsefulBox extends FoolEnchantableRelic implements OnSynergySubscriber, CustomSavable<Integer>
{
    public static final String ID = CreateFullID(UsefulBox.class);

    public UsefulBox()
    {
        this(null);
    }

    public UsefulBox(Enchantment enchantment)
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, 1, false, enchantment);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PCLCombatStats.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AbstractCard c)
    {
        PCLActions.Bottom.Draw(1);
        SetEnabled(false);
        flash();
    }
}