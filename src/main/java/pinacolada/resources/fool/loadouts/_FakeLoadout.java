package pinacolada.resources.fool.loadouts;

import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.basic.Defend;
import pinacolada.cards.fool.basic.Strike;
import pinacolada.cards.pcl.special.QuestionMark;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.resources.pcl.misc.PCLTrophies;

import java.util.ArrayList;

public class _FakeLoadout extends PCLLoadout
{
    public _FakeLoadout()
    {
        super("<Error>");
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(QuestionMark.DATA, 10);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return null;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return null;
    }

    @Override
    public void OnVictory(int ascensionLevel, int trophyLevel)
    {
        //
    }

    @Override
    public PCLTrophies GetTrophies()
    {
        return null;
    }
}
