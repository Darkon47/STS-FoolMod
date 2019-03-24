package eatyourbeets.characters.Loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Shimakaze;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.relics.PurgingStone;
import patches.AbstractEnums;

import java.util.ArrayList;

public class Kancolle extends AnimatorCustomLoadout
{
    public Kancolle()
    {
        Synergy s = Synergies.Kancolle;

        this.ID = s.ID;
        this.Name = s.NAME;
        this.StartingGold = 249;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(Shimakaze.ID);

        return res;
    }

    @Override
    protected String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return trophyStrings[3];
        }
        else if (trophy == 2)
        {
            return trophyStrings[6];
        }
        else if (trophy == 3)
        {
            return trophyStrings[7];
        }

        return null;
    }

    public void OnTrueVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
    {
        if (trophies == null)
        {
            trophies = GetTrophies(false);
        }

        if (AnimatorMetrics.lastLoadout == ID)
        {
            trophies.trophy1 = Math.max(trophies.trophy1, ascensionLevel);
        }

        ArrayList<Integer> synergies = new ArrayList<>();
        int uniqueSynergies = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
            if (card != null && card.color == AbstractEnums.Cards.THE_ANIMATOR)
            {
                Synergy synergy = card.GetSynergy();
                if (synergy != null)
                {
                    if (!synergies.contains(synergy.ID))
                    {
                        uniqueSynergies += 1;
                        synergies.add(synergy.ID);
                    }
                }
            }
        }

        if (uniqueSynergies >= 7)
        {
            trophies.trophy2 = Math.max(trophies.trophy2, ascensionLevel);
        }

        PurgingStone p = PurgingStone.GetInstance();
        if (uniqueSynergies >= 10 && p != null && p.GetBannedCount() == 0)
        {
            trophies.trophy3 = Math.max(trophies.trophy3, ascensionLevel);
        }
    }
}