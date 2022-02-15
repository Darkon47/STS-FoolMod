package pinacolada.interfaces.subscribers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.ui.combat.PCLAffinityMeter;

public interface OnTryChangeUltimateStateSubscriber
{
    boolean OnTryChangeUltimateState(AbstractPlayer p, PCLAffinityMeter meter, boolean isEntering);
}