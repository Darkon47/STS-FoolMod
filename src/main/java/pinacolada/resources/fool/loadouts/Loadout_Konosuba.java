package pinacolada.resources.fool.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.series.Konosuba.*;
import pinacolada.cards.fool.ultrarare.Chomusuke;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Konosuba extends PCLLoadout
{
    public Loadout_Konosuba()
    {
        super(CardSeries.Konosuba);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Dust.DATA, 4);
        AddStarterCard(Kazuma.DATA, 5);
        AddStarterCard(Mitsurugi.DATA, 6);
        AddStarterCard(Darkness.DATA, 7);
        AddStarterCard(Vanir.DATA, 7);
        AddStarterCard(Aqua.DATA, 8);
        AddStarterCard(YunYun.DATA, 9);
        AddStarterCard(Cecily.DATA, 10);
        AddStarterCard(Lean.DATA, 10);
        AddStarterCard(Verdia.DATA, 13);
        AddStarterCard(Hans.DATA, 14);
        AddStarterCard(Iris.DATA, 20);
        AddStarterCard(Wiz.DATA, 22);
        AddStarterCard(Megumin.DATA, 25);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Megumin.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Chomusuke.DATA;
    }
}
