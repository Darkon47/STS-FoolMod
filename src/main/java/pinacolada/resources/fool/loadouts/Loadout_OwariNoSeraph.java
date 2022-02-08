package pinacolada.resources.fool.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.series.OwariNoSeraph.*;
import pinacolada.cards.fool.ultrarare.HiiragiMahiru;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_OwariNoSeraph extends PCLLoadout
{
    public Loadout_OwariNoSeraph()
    {
        super(CardSeries.OwariNoSeraph);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Shigure.DATA, 5);
        AddStarterCard(KimizugiShiho.DATA, 6);
        AddStarterCard(Yoichi.DATA, 6);
        AddStarterCard(Mitsuba.DATA, 6);
        AddStarterCard(Shinoa.DATA, 7);
        AddStarterCard(CrowleyEusford.DATA, 8);
        AddStarterCard(Mikaela.DATA, 8);
        AddStarterCard(Yuuichirou.DATA, 8);
        AddStarterCard(LestKarr.DATA, 10);
        AddStarterCard(HiiragiShinya.DATA, 13);
        AddStarterCard(CrowleyEusford.DATA, 13);
        AddStarterCard(HiiragiKureto.DATA, 20);
        AddStarterCard(Guren.DATA, 32);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Yuuichirou.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return HiiragiMahiru.DATA;
    }
}
