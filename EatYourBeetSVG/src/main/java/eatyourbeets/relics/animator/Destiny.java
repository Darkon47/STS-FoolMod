package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.interfaces.subscribers.OnReceiveRewardsSubscriber;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class Destiny extends AnimatorRelic implements OnReceiveRewardsSubscriber, Hidden
{
    public static final String ID = CreateFullID(Destiny.class);

    public Destiny()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public int getPrice()
    {
        return 600;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards)
    {
        GameEffects.Queue.RemoveRelic(this);
    }
}