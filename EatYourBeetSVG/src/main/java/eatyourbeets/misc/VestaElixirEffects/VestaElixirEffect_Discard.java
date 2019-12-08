package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

public class VestaElixirEffect_Discard extends VestaElixirEffect
{
    private final VestaElixirEffect linkedEffect;

    public VestaElixirEffect_Discard(int index, VestaElixirEffect linkedEffect)
    {
        super(index, 2);

        this.linkedEffect = linkedEffect;
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        if (GameUtilities.GetOtherCardsInHand(elixir).size() >= amount)
        {
            GameActionsHelper.Discard(amount, false);
            linkedEffect.EnqueueAction(elixir, player);
        }
    }

    @Override
    public String GetDescription()
    {
        return super.GetDescription() + " NL " + linkedEffect.GetDescription();
    }
}