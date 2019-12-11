package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Attack_Buff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int block = GetBlock(nanami);
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        int strength = GetStrength(nanami);
        if (strength > 0)
        {
            GameActions.Bottom.GainForce(strength);
        }
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(BLOCK, GetBlock(nanami), true) + Desc(STRENGTH, GetStrength(nanami));
    }

    private static int GetStrength(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }

    private static int GetBlock(Nanami nanami)
    {
        int modifier = nanami.energyOnUse;

        if (modifier > 0)
        {
            int diff = (nanami.block - nanami.baseBlock);

            return ((nanami.energyOnUse + 1) * nanami.baseBlock) + diff;
        }
        else
        {
            return -1;
        }
    }
}