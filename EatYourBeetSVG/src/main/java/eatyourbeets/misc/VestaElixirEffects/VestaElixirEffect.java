package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.Utilities;

public abstract class VestaElixirEffect
{
    private final static String[] TEXT = Resources_Animator.GetCardStrings(Vesta_Elixir.ID).EXTENDED_DESCRIPTION;
    private final String rawDescription;

    public int amount;

    protected VestaElixirEffect(int descriptionIndex, int amount)
    {
        this.amount = amount;
        this.rawDescription = TEXT[descriptionIndex];
    }

    public String GetDescription()
    {
        return Utilities.Format(rawDescription, String.valueOf(amount));
    }

    public abstract void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player);
}