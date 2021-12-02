package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.Bleach.*;
import eatyourbeets.cards.animator.beta.ultrarare.SosukeAizen;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Bleach extends AnimatorLoadout
{
    public Loadout_Bleach()
    {
        super(CardSeries.Bleach);

        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(RenjiAbarai.DATA, 4);
        AddStarterCard(OrihimeInoue.DATA, 4);
        AddStarterCard(YasutoraSado.DATA, 5);
        AddStarterCard(UryuuIshida.DATA, 5);
        AddStarterCard(IchigoKurosaki.DATA, 7);
        AddStarterCard(RukiaKuchiki.DATA, 11);
        AddStarterCard(ByakuyaKuchiki.DATA, 20);
        AddStarterCard(Status_Wound.DATA, -4);
    }


    @Override
    public EYBCardData GetSymbolicCard()
    {
        return IchigoKurosaki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SosukeAizen.DATA;
    }
}
