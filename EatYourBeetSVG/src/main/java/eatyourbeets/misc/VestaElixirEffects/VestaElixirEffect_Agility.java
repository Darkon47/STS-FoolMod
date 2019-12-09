package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class VestaElixirEffect_Agility extends VestaElixirEffect
{
    public VestaElixirEffect_Agility(int index)
    {
        super(index, AbstractDungeon.cardRandomRng.randomBoolean(0.33f) ? 3 : 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper_Legacy.ApplyPower(player, player, new AgilityPower(player, amount), amount);
    }
}