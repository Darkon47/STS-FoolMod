package eatyourbeets.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergy;

import java.util.Collections;
import java.util.Comparator;

public class CustomCardLibSortHeader extends CardLibSortHeader
{
    private SortHeaderButton[] override = null;
    private SortHeaderButton seriesButton = null;
    private SortHeaderButton rarityButton;
    private SortHeaderButton typeButton;
    private SortHeaderButton nameButton;
    private SortHeaderButton costButton;

    public CustomCardLibSortHeader(CardGroup group)
    {
        super(group);
    }

    private void SetupButton(SortHeaderButton button, float offsetX, int index)
    {
        override[index] = button;

        Hitbox hitbox = button.hb;
        hitbox.resize(hitbox.width + offsetX, hitbox.height);
        hitbox.move(hitbox.cX + (offsetX * index), hitbox.cY);
    }

    public void SetupButtons()
    {
        if (override == null)
        {
            float START_X = 430.0F * Settings.scale;
            float SPACE_X = (226.0F * Settings.scale);
            float xPosition = START_X + (SPACE_X * buttons.length);

            override = new SortHeaderButton[5];

            rarityButton = buttons[0];
            typeButton = buttons[1];
            nameButton = buttons[2];
            costButton = buttons[3];
            seriesButton = new SortHeaderButton("Series", xPosition, 0.0F, this);

            float offsetX = -(Settings.scale * 30f);

            SetupButton(rarityButton, offsetX, 0);
            SetupButton(typeButton, offsetX, 1);
            SetupButton(nameButton, offsetX, 2);
            SetupButton(costButton, offsetX, 3);
            SetupButton(seriesButton, offsetX, 4);
        }

        buttons = override;
    }

    @Override
    public void setGroup(CardGroup group)
    {
        super.setGroup(group);

//        for (AbstractCard card : AnimatorCard_UltraRare.GetCards().values())
//        {
//            if (UnlockTracker.isCardSeen(card.cardID))
//            {
//                if (!group.contains(card))
//                {
//                    group.group.add(card);
//                }
//            }
//        }
    }

    @Override
    public void didChangeOrder(SortHeaderButton button, boolean isAscending)
    {
        if (button == this.rarityButton)
        {
            this.group.sortByRarity(isAscending);
        }
        else if (button == this.typeButton)
        {
            this.group.sortByType(isAscending);
        }
        else if (button == this.nameButton)
        {
            this.group.sortAlphabetically(isAscending);
        }
        else if (button == this.seriesButton)
        {
            if (!isAscending)
            {
                this.group.group.sort(new CardSeriesComparator());
            }
            else
            {
                this.group.group.sort(Collections.reverseOrder(new CardSeriesComparator()));
            }
        }
        else
        {
            if (button != this.costButton)
            {
                return;
            }

            this.group.sortByCost(isAscending);
        }

        this.group.sortByStatus(false);
        this.justSorted = true;
    }

    private class CardSeriesComparator implements Comparator<AbstractCard>
    {
        private CardSeriesComparator()
        {
        }

        public int compare(AbstractCard c1, AbstractCard c2)
        {
            AnimatorCard a1 = Utilities.SafeCast(c1, AnimatorCard.class);
            AnimatorCard a2 = Utilities.SafeCast(c2, AnimatorCard.class);

            if (a1 == null)
            {
                return a2 == null ? 0 : -1;
            }
            else if (a2 == null)
            {
                return 1;
            }

            if (a1.rarity == AbstractCard.CardRarity.BASIC || a1.rarity == AbstractCard.CardRarity.SPECIAL)
            {
                return -1;
            }
            else if (a2.rarity == AbstractCard.CardRarity.BASIC || a2.rarity == AbstractCard.CardRarity.SPECIAL)
            {
                return +1;
            }

            Synergy s1 = a1.GetSynergy();
            Synergy s2 = a2.GetSynergy();

            if (s1 == null)
            {
                return s2 == null ? 0 : -1;
            }
            else if (s2 == null)
            {
                return 1;
            }

            return s1.NAME.compareTo(s2.NAME);
        }
    }
}
