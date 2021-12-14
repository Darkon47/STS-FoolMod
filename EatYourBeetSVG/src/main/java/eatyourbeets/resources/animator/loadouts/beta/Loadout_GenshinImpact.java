package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.curse.Curse_SearingBurn;
import eatyourbeets.cards.animator.beta.series.GenshinImpact.*;
import eatyourbeets.cards.animator.beta.ultrarare.Dainsleif;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_GenshinImpact extends AnimatorLoadout
{
    public Loadout_GenshinImpact()
    {
        super(CardSeries.GenshinImpact);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Amber.DATA, 5);
        AddStarterCard(Noelle.DATA, 5);
        AddStarterCard(LisaMinci.DATA, 6);
        AddStarterCard(KaeyaAlberich.DATA, 7);
        AddStarterCard(Fischl.DATA, 10);
        AddStarterCard(Klee.DATA, 14);
        AddStarterCard(JeanGunnhildr.DATA, 20);
        AddStarterCard(Venti.DATA, 25);
        AddStarterCard(Curse_SearingBurn.DATA, -6);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Venti.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Dainsleif.DATA;
    }
}
