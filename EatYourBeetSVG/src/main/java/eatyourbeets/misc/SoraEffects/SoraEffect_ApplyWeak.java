package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_ApplyWeak extends SoraEffect
{
    public SoraEffect_ApplyWeak(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            if (!m.isDead && !m.isDying)
            {
                GameActions.Bottom.ApplyWeak(player, m, sora.magicNumber);
            }
        }
    }
}