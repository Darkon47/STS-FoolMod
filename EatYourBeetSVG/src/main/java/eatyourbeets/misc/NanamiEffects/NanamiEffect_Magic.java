package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Magic extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int orbs = GetOrbs(nanami);

        for (int i = 0; i < orbs; i++)
        {
            GameActionsHelper.ChannelRandomOrb(true);
        }
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(RANDOM_ORB, GetOrbs(nanami));
    }

    private static int GetOrbs(Nanami nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}