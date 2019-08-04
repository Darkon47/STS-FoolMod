package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Energy extends VestaElixirEffect
{
    public VestaElixirEffect_Energy(int index)
    {
        super(index, 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.GainEnergy(amount);
    }
}