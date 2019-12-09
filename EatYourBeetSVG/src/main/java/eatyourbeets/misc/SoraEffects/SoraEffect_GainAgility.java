package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_GainAgility extends SoraEffect
{
    public SoraEffect_GainAgility(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper_Legacy.ApplyPower(player, player, new AgilityPower(player, sora.magicNumber), sora.magicNumber);
    }
}