package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AnimatorLoadout
{
    public static final int MAX_VALUE = 30;
    public static final int MIN_CARDS = 10;

    public CardSlots Slots;
    public CardSlot SpecialSlot1;
    public CardSlot SpecialSlot2;

    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = "";

    public int ID;
    public String Name;
    public CardSeries Series;
    public boolean IsBeta;

    public int StartingGold = 99;
    public int MaxHP = 60;
    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name)
    {
        this.IsBeta = true;
        this.Series = null;
        this.Name = name;
        this.ID = -1;
    }

    public AnimatorLoadout(CardSeries series)
    {
        this.IsBeta = false;
        this.Series = series;
        this.Name = series.LocalizedName;
        this.ID = series.ID;
    }

    public abstract EYBCardData GetSymbolicCard();
    public abstract EYBCardData GetUltraRare();

    public void InitializeSlots()
    {
        Slots = new CardSlots();
        Slots.AddSlot(1, 6).AddItem(Defend.DATA, -2);
        Slots.AddSlot(1, 6).AddItem(Strike.DATA, -2);
        Slots.AddSlot(0, 1).AddItems(ImprovedStrike.GetCards(), 1);
        Slots.AddSlot(0, 1).AddItems(ImprovedDefend.GetCards(), 1);
        SpecialSlot1 = Slots.AddSlot(0, 1);
        SpecialSlot2 = Slots.AddSlot(0, 1);
    }

    public void LoadStartingDeck()
    {
        if (Slots.Ready)
        {
            return;
        }

        Slots.Get(0).Select(0, 4).GetData().MarkSeen();
        Slots.Get(1).Select(0, 4).GetData().MarkSeen();
        Slots.Get(2).Select(null);
        JUtils.ForEach(ImprovedStrike.GetCards(), EYBCardData::MarkSeen);
        Slots.Get(3).Select(null);
        JUtils.ForEach(ImprovedDefend.GetCards(), EYBCardData::MarkSeen);
        Slots.Get(4).Select(0, 1).GetData().MarkSeen();
        Slots.Get(5).Select(1, 1).GetData().MarkSeen();
        Slots.Ready = true;
    }

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter animatorCharacter)
    {
        return new CharSelectInfo(name + "-" + ID, description, MaxHP, MaxHP, OrbSlots, StartingGold, CardDraw,
                                        animatorCharacter, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        for (CardSlot slot : Slots)
        {
            EYBCardData data = slot.GetData();
            if (data != null)
            {
                for (int i = 0; i < slot.amount; i++)
                {
                    cards.add(data.ID);
                }
            }
        }

        if (cards.isEmpty())
        {
            for (int i = 0; i < 5; i++)
            {
                cards.add(Strike.DATA.ID);
                cards.add(Defend.DATA.ID);
            }
        }

        return cards;
    }

    public ArrayList<String> GetStartingRelics()
    {
        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(TheMissingPiece.ID);
        return res;
    }

    public AnimatorTrophies GetTrophies()
    {
        if (IsBeta)
        {
            return null;
        }

        AnimatorTrophies trophies = GR.Animator.Data.GetTrophies(ID);
        if (trophies == null)
        {
            trophies = new AnimatorTrophies(ID);
            GR.Animator.Data.Trophies.add(trophies);
        }

        return trophies;
    }

    public String GetDeckPreviewString()
    {
// TODO: Loadout preview string
//
//        if (shortDescription == null)
//        {
//            StringJoiner sj = new StringJoiner(", ");
//            for (String s : GetStartingDeck())
//            {
//                if (!s.contains(Strike.DATA.ID) && !s.contains(Defend.DATA.ID))
//                {
//                    sj.add(CardLibrary.getCard(s).originalName);
//                }
//            }
//
//            shortDescription = sj.toString();
//        }

        return shortDescription;
    }

    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Strings.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Strings.Trophies.SilverDescription;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Strings.Trophies.GoldDescription;
        }

        return null;
    }

    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        AnimatorTrophies trophies = GetTrophies();

        if (GR.Animator.Data.SelectedLoadout.ID == ID)
        {
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }

        ArrayList<String> cardsWithSynergy = new ArrayList<>();
        int synergyCount = 0;
        int uniqueCards = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                CardSeries series = card.series;
                if (series != null && series.ID == ID)
                {
                    synergyCount += 1;
                    if (!cardsWithSynergy.contains(card.cardID) && card.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        uniqueCards += 1;
                        cardsWithSynergy.add(card.cardID);
                    }
                }
            }
        }

        if (synergyCount >= cards.size() / 2)
        {
            trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
        }

        if (uniqueCards >= 8)
        {
            trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
        }
    }

    protected void AddToSpecialSlots(EYBCardData data, int estimatedValue)
    {
        SpecialSlot1.AddItem(data, estimatedValue);
        SpecialSlot2.AddItem(data, estimatedValue);
    }
}