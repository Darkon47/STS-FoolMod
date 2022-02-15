package pinacolada.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.StringJoiner;

public class CardPoolPanelItem extends PCLTopPanelItem
{
    public static final String ID = CreateFullID(CardPoolPanelItem.class);
    protected CardGroup cardGroup;

    public CardPoolPanelItem() {
        super(PGR.PCL.Images.CardPool, ID);
        SetTooltip(new PCLCardTooltip(PGR.PCL.Strings.Misc.ViewCardPool, PGR.PCL.Strings.Misc.ViewCardPoolDescription));
    }

    @Override
    protected void onClick() {
        super.onClick();

        PGR.UI.CardsScreen.Open(GetAllCards());
    }

    protected CardGroup GetAllCards() {
        if (cardGroup == null) {
            cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        }
        if (PCLGameUtilities.GetTotalCardsInPlay() != cardGroup.size()) {
            cardGroup.clear();
            for (CardGroup cg: PCLGameUtilities.GetSourceCardPools()) {
                for (AbstractCard c : cg.group) {
                    cardGroup.addToTop(c);
                }
            }
        }

        return cardGroup;
    }

    @Override
    public void update() {
        super.update();
        if (this.tooltip != null && getHitbox().hovered) {
            tooltip.description = GetFullDescription();
            PCLCardTooltip.QueueTooltip(tooltip);
        }
    }

    public String GetFullDescription()
    {
        String base = PGR.PCL.Strings.Misc.ViewCardPoolDescription;
        if (PGR.PCL.Dungeon.Loadouts.isEmpty())
        {
            return base;
        }

        StringJoiner joiner = new StringJoiner(" NL ");
        for (PCLRuntimeLoadout series : PGR.PCL.Dungeon.Loadouts)
        {
            String line;
            if (PGR.Fool.Data.SelectedLoadout.Series.equals(series.Loadout.Series))
            {
                line = "- #y" + PCLJUtils.ModifyString(series.Loadout.Name, w -> "#y" + w);
            }
            else
            {
                line = "- " + series.Loadout.Name;
            }

            if (series.bonus > 0)
            {
                line += " #y( " + series.bonus + "/6 #y)";
            }

            joiner.add(line);
        }

        return base + " NL  NL " + PCLJUtils.ModifyString(PGR.PCL.Strings.Misc.ViewCardPoolSeries, w -> "#p" + w) + ": NL " + joiner;
    }
}
