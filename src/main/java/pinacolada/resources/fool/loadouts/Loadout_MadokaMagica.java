package pinacolada.resources.fool.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.curse.Curse_GriefSeed;
import pinacolada.cards.fool.series.MadokaMagica.*;
import pinacolada.cards.fool.ultrarare.Walpurgisnacht;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_MadokaMagica extends PCLLoadout
{
    public Loadout_MadokaMagica()
    {
        super(CardSeries.MadokaMagica);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(FeliciaMitsuki.DATA, 4);
        AddStarterCard(OrikoMikuni.DATA, 4);
        AddStarterCard(IrohaTamaki.DATA, 5);
        AddStarterCard(KyokoSakura.DATA, 7);
        AddStarterCard(YuiTsuruno.DATA, 7);
        AddStarterCard(SuzuneAmano.DATA, 9);
        AddStarterCard(NagisaMomoe.DATA, 10);
        AddStarterCard(SayakaMiki.DATA, 10);
        AddStarterCard(AlinaGray.DATA, 12);
        AddStarterCard(MamiTomoe.DATA, 14);
        AddStarterCard(NagisaMomoe.DATA, 15);
        AddStarterCard(MifuyuAsuza.DATA, 16);
        AddStarterCard(MadokaKaname.DATA, 25);
        AddStarterCard(Curse_GriefSeed.DATA, 0);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return MadokaKaname.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Walpurgisnacht.DATA;
    }
}