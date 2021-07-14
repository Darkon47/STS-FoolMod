package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Map;

public class AnimatorRuntimeLoadout
{
    private final static EYBCardTooltip PromotedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.PickupBonusHeader, GR.Animator.Strings.SeriesSelection.PickupBonusBody);
    private final static float AFFINITY_THRESHOLD = 0.66f;
    private final static int MAX_AFFINITIES = 2;

    public final int ID;
    public final boolean IsBeta;
    public final AnimatorLoadout Loadout;
    public final Map<String, AbstractCard> Cards;
    public final EYBCardAffinityStatistics AffinityStatistics;

    public int bonus;
    public AnimatorCard card;
    public boolean promoted;

    public static AnimatorRuntimeLoadout TryCreate(AnimatorLoadout loadout)
    {
        // Starting series of level 5 of more are not selectable until unlocked
        if (loadout != null && loadout.UnlockLevel <= Math.max(5, GR.Animator.GetUnlockLevel()))
        {
            AnimatorRuntimeLoadout result = new AnimatorRuntimeLoadout(loadout);
            if (result.Cards.size() > 0 && result.Loadout.GetSymbolicCard() != null)
            {
                return result;
            }
        }

        return null;
    }

    public AnimatorRuntimeLoadout(AnimatorLoadout loadout)
    {
        this.ID = loadout.ID;
        this.IsBeta = loadout.IsBeta;
        this.Loadout = loadout;
        this.Cards = GetNonColorlessCards(loadout.Series);
        this.AffinityStatistics = new EYBCardAffinityStatistics(Cards.values());

        this.promoted = false;
        this.card = null;
    }

    public void Promote()
    {
        if (card != null)
        {
            throw new RuntimeException("Can not promote a card that has already been built.");
        }

        this.promoted = true;
    }

    public AbstractCard BuildCard()
    {
        EYBCardData data = Loadout.GetSymbolicCard();
        if (data == null)
        {
            JUtils.LogInfo(this, Loadout.Name + " has no symbolic card.");
            return null;
        }

        AbstractCard temp = data.CreateNewInstance();
        AnimatorCardBuilder builder = new AnimatorCardBuilder(String.valueOf(Loadout.ID)).SetImage(temp.assetUrl).CanUpgrade(false);

        if (promoted)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Promoted(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(PromotedTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Beta(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
        }
        else
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE).Build();
        }

        int i = 0;
        for (EYBCardAffinityStatistics.Group g : AffinityStatistics)
        {
            if (i++ > MAX_AFFINITIES)
            {
                break;
            }

            float percentage = g.GetPercentage(0);
            if (percentage > 0)
            {
                card.affinities.Add(g.Type, percentage > AFFINITY_THRESHOLD ? 2 : 1);
            }
        }

        return card;
    }

    private Map<String, AbstractCard> GetNonColorlessCards(CardSeries series)
    {
        Map<String, AbstractCard> cards = new HashMap<>();

        if (series != null && series != CardSeries.ANY)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
                if (c != null && card.color == GR.Animator.CardColor && series.equals(c.series)
                    && card.rarity != AbstractCard.CardRarity.SPECIAL
                    && card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    cards.put(c.cardID, c);
                }
            }
        }

        return cards;
    }
}
