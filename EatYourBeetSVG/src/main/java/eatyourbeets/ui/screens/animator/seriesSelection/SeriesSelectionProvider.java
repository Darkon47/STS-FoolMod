package eatyourbeets.ui.screens.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.colorless.rare.Emilia;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.animator.colorless.uncommon.Urushihara;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._Test;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SeriesSelectionProvider
{
    public int TotalCardsInPool = 0;
    public final Map<AbstractCard, SeriesSelectionItem> cardsMap = new HashMap<>();
    public final ArrayList<AbstractCard> promotedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    public final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    public final ArrayList<AbstractCard> allCards = new ArrayList<>();

    public static void PreloadResources()
    {
        new SeriesSelectionProvider().CreateCards();
        CardCrawlGame.sound.preload("CARD_SELECT");
    }

    public void CreateCards()
    {
        TotalCardsInPool = 0;

        cardsMap.clear();
        selectedCards.clear();
        promotedCards.clear();
        betaCards.clear();
        allCards.clear();

        int promotedCount = 0;
        RandomizedList<SeriesSelectionItem> toPromote = new RandomizedList<>();
        ArrayList<SeriesSelectionItem> seriesSelectionItems = new ArrayList<>();
        for (AnimatorLoadout loadout : GR.Animator.Database.BaseLoadouts)
        {
            SeriesSelectionItem card = SeriesSelectionItem.TryCreate(loadout);
            if (card != null)
            {
                if (loadout == GR.Animator.Database.SelectedLoadout)
                {
                    card.Promote();
                    promotedCount += 1;
                }
                else
                {
                    toPromote.Add(card);
                }

                seriesSelectionItems.add(card);
            }
        }

        // <Beta>

        seriesSelectionItems.add(SeriesSelectionItem.TryCreate(new _Test(Synergies.HatarakuMaouSama, Urushihara.ID, 4)));
        seriesSelectionItems.add(SeriesSelectionItem.TryCreate(new _Test(Synergies.ReZero, Emilia.ID, 5)));
        seriesSelectionItems.add(SeriesSelectionItem.TryCreate(new _Test(Synergies.Jojo, QuestionMark.ID, 7)));

        // </Beta>

        Random rng = new Random(Settings.seed + 13);
        while (promotedCount < 3)
        {
            toPromote.Retrieve(rng).Promote();
            promotedCount += 1;
        }

        for (SeriesSelectionItem c : seriesSelectionItems)
        {
            cardsMap.put(c.BuildCard(), c);
        }
    }

    public SeriesSelectionItem Find(AbstractCard card)
    {
        return cardsMap.get(card);
    }

    public Collection<SeriesSelectionItem> GetAllCards()
    {
        return cardsMap.values();
    }

    public boolean Select(AbstractCard card)
    {
        if (!selectedCards.contains(card))
        {
            TotalCardsInPool += Find(card).size;
            selectedCards.add(card);
            return true;
        }

        return false;
    }

    public boolean Deselect(AbstractCard card)
    {
        SeriesSelectionItem c = Find(card);
        if (!c.promoted && selectedCards.remove(card))
        {
            TotalCardsInPool -= c.size;
            return true;
        }

        return false;
    }

    public void UpdateDatabase()
    {
        // TODO:
    }
}
