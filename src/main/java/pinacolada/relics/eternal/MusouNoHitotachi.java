package pinacolada.relics.eternal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.interfaces.subscribers.OnTryGainResolveSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.EternalRelic;
import pinacolada.ui.combat.EternalResolveMeter;
import pinacolada.utilities.PCLActions;

public class MusouNoHitotachi extends EternalRelic implements OnSynergySubscriber, OnTryGainResolveSubscriber
{
    public static final String ID = CreateFullID(MusouNoHitotachi.class);

    public MusouNoHitotachi()
    {
        super(ID, RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLCombatStats.onTryGainResolve.Subscribe(this);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PCLCombatStats.onSynergy.SubscribeOnce(this);
        SetCounter(1);
    }

    @Override
    public int OnTryGainResolve(AbstractCard card, AbstractPlayer p, int originalCost, boolean isActuallyGaining) {
        if (PCLCombatStats.MatchingSystem.ResolveMeter.Resolve() >= EternalResolveMeter.MAX_RESOLVE) {
            return 0;
        }
        return originalCost;
    }

    @Override
    public void OnSynergy(AbstractCard c)
    {
        PCLActions.Bottom.Draw(1);
        SetCounter(0);
        flash();
    }
}