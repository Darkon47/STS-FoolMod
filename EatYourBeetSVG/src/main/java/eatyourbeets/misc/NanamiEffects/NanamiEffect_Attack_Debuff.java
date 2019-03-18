package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Attack_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int block = GetBlock(nanami);
        if (block > 0)
        {
            GameActionsHelper.GainBlock(p, block);
        }

        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, GetWeak(nanami), false), GetWeak(nanami));
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(BLOCK, GetBlock(nanami), true) + Desc(WEAK, GetWeak(nanami));
    }

    private static int GetWeak(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }

    private static int GetBlock(Nanami nanami)
    {
        int modifier = nanami.energyOnUse;

        if (modifier > 0)
        {
            return (modifier + 1) * nanami.block;
        }
        else
        {
            return -1;
        }
    }
}