package pinacolada.resources.fool.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.series.GoblinSlayer.*;
import pinacolada.cards.fool.ultrarare.DemonLord;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_GoblinSlayer extends PCLLoadout
{
    public Loadout_GoblinSlayer()
    {
        super(CardSeries.GoblinSlayer);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Fighter.DATA, 5);
        AddStarterCard(HighElfArcher.DATA, 5);
        AddStarterCard(DwarfShaman.DATA, 6);
        AddStarterCard(LizardPriest.DATA, 6);
        AddStarterCard(Priestess.DATA, 7);
        AddStarterCard(CowGirl.DATA, 8);
        AddStarterCard(Spearman.DATA, 8);
        AddStarterCard(Witch.DATA, 9);
        AddStarterCard(NobleFencer.DATA, 10);
        AddStarterCard(ApprenticeCleric.DATA, 13);
        AddStarterCard(SwordMaiden.DATA, 20);
        AddStarterCard(HeavyWarrior.DATA, 22);
        AddStarterCard(GoblinSlayer.DATA, 27);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return pinacolada.cards.fool.series.GoblinSlayer.GoblinSlayer.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return DemonLord.DATA;
    }
}
