package pinacolada.relics.eternal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnTryChangeUltimateStateSubscriber;
import pinacolada.interfaces.subscribers.OnTryGainResolveSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.EternalRelic;
import pinacolada.ui.combat.EternalResolveMeter;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class MusouNoHitotachi extends EternalRelic implements OnTryGainResolveSubscriber, OnTryChangeUltimateStateSubscriber
{
    public static final String ID = CreateFullID(MusouNoHitotachi.class);
    public static final int DAMAGE = 10;

    public MusouNoHitotachi()
    {
        super(ID, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLCombatStats.onTryGainResolve.Subscribe(this);
        PCLCombatStats.onTryChangeUltimateState.Subscribe(this);
    }

    @Override
    public int OnTryGainResolve(AbstractCard card, AbstractPlayer p, int originalCost, boolean isActuallyGaining, boolean isMatching) {
        if (isMatching && PCLCombatStats.MatchingSystem.ResolveMeter.Resolve() >= EternalResolveMeter.MAX_RESOLVE) {
            return 0;
        }
        return originalCost;
    }

    @Override
    public boolean OnTryChangeUltimateState(AbstractPlayer p, PCLAffinityMeter meter, boolean isEntering) {
        if (isEntering) {
            int[] damage = DamageInfo.createDamageMatrix(DAMAGE, false, true);
            PCLActions.Delayed.DealDamageToAll(damage, DamageInfo.DamageType.NORMAL, AttackEffects.DARKNESS)
                    .ApplyPowers(true);
        }

        return true;
    }
}