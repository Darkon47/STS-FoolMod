package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper2;


public class AinzEffect_ChannelRandomOrbs extends AinzEffect
{
    public AinzEffect_ChannelRandomOrbs(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 3 : 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        for (int i = 0; i < ainz.magicNumber; i++)
        {
            GameActionsHelper2.ChannelRandomOrb(true);
        }
    }
}