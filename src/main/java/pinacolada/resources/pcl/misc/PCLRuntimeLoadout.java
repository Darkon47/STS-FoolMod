package pinacolada.resources.pcl.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static pinacolada.resources.PGR.Enums.CardTags.EXPANDED;

public class PCLRuntimeLoadout
{
    private final static PCLCardTooltip BetaTooltip = new PCLCardTooltip(PGR.PCL.Strings.SeriesSelection.Beta, PGR.PCL.Strings.SeriesSelection.TooltipBeta);
    private final static PCLCardTooltip ExpansionTooltip = new PCLCardTooltip(PGR.PCL.Strings.SeriesSelection.ExpansionHeader, PGR.PCL.Strings.SeriesSelection.ExpansionCardBody);

    public final int ID;
    public final boolean IsBeta;
    public final PCLLoadout Loadout;
    public final PCLTrophies Trophies;
    public final Map<String, AbstractCard> BaseCards = new HashMap<>();
    public final Map<String, AbstractCard> ExpandedCards = new HashMap<>();
    public final PCLCardTooltip Trophy1Tooltip;
    public final PCLCardTooltip Trophy2Tooltip;
    public final PCLCardTooltip Trophy3Tooltip;
    public final PCLCardTooltip UnlockTooltip;

    public int bonus;
    public PCLCard card;
    public PCLCardAffinityStatistics AffinityStatistics;
    public boolean expansionEnabled;
    public boolean canEnableExpansion;
    public boolean isLocked;

    public static PCLRuntimeLoadout TryCreate(PCLLoadout loadout)
    {
        PCLRuntimeLoadout result = new PCLRuntimeLoadout(loadout);
        if (result.GetCardPoolInPlay().size() > 0 && result.Loadout.GetSymbolicCard() != null)
        {
            return result;
        }

        return null;
    }

    public PCLRuntimeLoadout(PCLLoadout loadout)
    {
        this.ID = loadout.ID;
        this.IsBeta = loadout.IsBeta;
        this.Loadout = loadout;
        this.canEnableExpansion = loadout.CanEnableExpansion();
        this.Trophies = Loadout.GetTrophies();

        this.expansionEnabled = PGR.PCL.Config.ExpandedSeries.Get().contains(loadout.Series);
        this.Trophy1Tooltip = new PCLCardTooltip(PGR.PCL.Strings.Trophies.Bronze, Trophies != null && Trophies.Trophy1 >= 0 ? PGR.PCL.Strings.Trophies.BronzeFormatted(Trophies.Trophy1) : PGR.PCL.Strings.Trophies.BronzeLocked);
        this.Trophy2Tooltip = new PCLCardTooltip(PGR.PCL.Strings.Trophies.Silver, Trophies != null && Trophies.Trophy2 >= 0 ? PGR.PCL.Strings.Trophies.SilverFormatted(Trophies.Trophy2) : PGR.PCL.Strings.Trophies.SilverLocked);
        this.Trophy3Tooltip = new PCLCardTooltip(PGR.PCL.Strings.Trophies.Gold, Trophies != null && Trophies.Trophy3 >= 0 ? PGR.PCL.Strings.Trophies.GoldFormatted(Trophies.Trophy3) : PGR.PCL.Strings.Trophies.GoldLocked);
        this.UnlockTooltip = new PCLCardTooltip(PGR.PCL.Strings.CharSelect.RightText, PGR.PCL.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, PGR.Fool.GetUnlockLevel()));
        InitializeCards(loadout.Series);

        this.AffinityStatistics = new PCLCardAffinityStatistics(GetCardPoolInPlay().values(), false);

        this.card = null;
        this.isLocked = PGR.Fool.GetUnlockLevel() < Loadout.UnlockLevel;
    }

    public CardGroup GetCardPool()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : GetCardPoolInPlay().values())
        {
            final CardGroup pool = PCLGameUtilities.GetCardPool(c.rarity);
            if (pool != null)
            {
                c = pool.findCardById(c.cardID);

                if (c != null)
                {
                    final AbstractCard copy = c.makeCopy();
                    copy.isSeen = c.isSeen;
                    group.group.add(copy);
                }
            }
        }

        group.sortByRarity(true);

        return group;
    }

    public void ToggleExpansion(boolean value) {
        if (!this.canEnableExpansion) {
            value = false;
            return;
        }
        this.expansionEnabled = value;
        this.AffinityStatistics = new PCLCardAffinityStatistics(GetCardPoolInPlay().values(), false);
        if (card != null) {
            card.upgraded = value;
            card.cardText.ForceRefresh();
            card.tooltips.add(ExpansionTooltip);
            if (Loadout.IsBeta) {
                card.tooltips.add(BetaTooltip);
            }
        }
    }

    public Map<String, AbstractCard> GetCardPoolInPlay() {
        return canEnableExpansion && expansionEnabled ? ExpandedCards : BaseCards;
    }

    public ArrayList<AbstractCard> GetSeenCards(Map<String, AbstractCard> source) {
        return JUtils.Filter(source.values(), c -> c.isSeen);
    }

    public AbstractCard GetRandomCard() {
        return PCLGameUtilities.GetRandomElement(new ArrayList<>(GetCardPoolInPlay().values()), PCLCard.rng);
    }

    public AbstractCard GetWeightedRandomCard(boolean inCombat) {
        WeightedList<AbstractCard> rewards = new WeightedList<>();

        for (AbstractCard c : GetCardPoolInPlay().values())
        {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && (!inCombat || PCLGameUtilities.IsObtainableInCombat(c)))
            {
                switch (c.rarity)
                {
                    case COMMON:
                        rewards.Add(c, 45);
                        break;

                    case UNCOMMON:
                        rewards.Add(c, 40);
                        break;

                    case RARE:
                        rewards.Add(c, 20);
                        break;
                }
            }
        }
        return rewards.Retrieve(PCLCard.rng);
    }

    public AbstractCard BuildCard()
    {
        final PCLCardData data = Loadout.GetSymbolicCard();
        if (data == null)
        {
            PCLJUtils.LogInfo(this, Loadout.Name + " has no symbolic card.");
            return null;
        }

        final AbstractCard temp = data.CreateNewInstance();
        final PCLCardBuilder builder = new PCLCardBuilder(String.valueOf(Loadout.ID))
                .SetImagePath(temp.assetUrl)
                .ShowTypeText(false)
                .SetNumbers(0,0,BaseCards.size(),ExpandedCards.size(),0)
                .SetUpgrades(0, 0, ExpandedCards.size() - BaseCards.size(), 0, 0)
                .CanUpgrade(false);

        if (isLocked) {
            card = builder
                    .SetText(Loadout.Name, UnlockTooltip.description, null)
                    .SetProperties(AbstractCard.CardType.CURSE, AbstractCard.CardRarity.COMMON).Build();
            card.tooltips.add(UnlockTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name,
                    PGR.PCL.Strings.SeriesSelection.ContainsNCards_Beta("!M!"),
                    PGR.PCL.Strings.SeriesSelection.ContainsNCards_Beta("!S!"))
            .SetProperties(AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON).Build();
            card.tooltips.add(BetaTooltip);
        }
        else
        {
            String trophyString1 = (Trophies.Trophy1 > -1 ? Trophies.Trophy1 : "--") + "/" + PCLTrophies.MAXIMUM_TROPHY;
            String trophyString2 = (Trophies.Trophy2 > -1 ? Trophies.Trophy2 : "--") + "/" + PCLTrophies.MAXIMUM_TROPHY;
            String trophyString3 = (Trophies.Trophy3 > -1 ? Trophies.Trophy3 : "--") + "/" + PCLTrophies.MAXIMUM_TROPHY;
            card = builder
            .SetText(Loadout.Name,
                    PGR.PCL.Strings.SeriesSelection.ContainsNCards_Full("!M!", trophyString1, trophyString2, trophyString3),
                    PGR.PCL.Strings.SeriesSelection.ContainsNCards_Full("!S!", trophyString1, trophyString2, trophyString3))
            .SetProperties(AbstractCard.CardType.SKILL, AbstractCard.CardRarity.COMMON).Build();
            card.tooltips.add(Trophy1Tooltip);
            card.tooltips.add(Trophy2Tooltip);
            card.tooltips.add(Trophy3Tooltip);
        }

        if (canEnableExpansion) {
            card.tags.add(EXPANDED);
            card.tooltips.add(ExpansionTooltip);
        }

        int i = 0;
        int maxLevel = 2;
        float maxPercentage = 0;
        for (PCLCardAffinityStatistics.Group g : AffinityStatistics)
        {
            float percentage = g.GetPercentage(0);
            if (percentage == 0 || i > 2)
            {
                break;
            }

            if (percentage < maxPercentage || (maxLevel == 2 && percentage < 0.3f))
            {
                maxLevel -= 1;
            }
            if (maxLevel > 0)
            {
                card.affinities.Add(g.Affinity, maxLevel);
            }

            maxPercentage = percentage;
            i += 1;
        }
        card.affinities.collapseDuplicates = true;
        card.affinities.UpdateSortedList();

        return card;
    }

    private void InitializeCards(CardSeries series)
    {
        if (series != null && series != CardSeries.COLORLESS)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
                if (c != null && card.color == PGR.Fool.CardColor && (series.equals(c.series) || series.equals(CardSeries.ANY))
                    && card.rarity != AbstractCard.CardRarity.SPECIAL
                    && card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    ExpandedCards.put(c.cardID, c);
                    if (!c.cardData.IsExpansionCard) {
                        BaseCards.put(c.cardID, c);
                    }
                }
            }
        }
    }
}
