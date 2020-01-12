package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_NoGameNoLife;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_NoGameNoLife;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaCouronne;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaSchwi;
import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.cards.animator.ultrarare.Azriel;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

import java.util.ArrayList;

public class NoGameNoLife extends AnimatorLoadout
{
    public NoGameNoLife()
    {
        super(Synergies.NoGameNoLife);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_NoGameNoLife.ID);
            startingDeck.add(Defend_NoGameNoLife.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(DolaSchwi.ID);
            startingDeck.add(DolaCouronne.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Sora.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Azriel();
        }

        return ultraRare;
    }
}
