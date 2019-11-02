package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseAnyNumberFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class Geryuganshoop extends AnimatorCard
{
    public static final String ID = Register(Geryuganshoop.class.getSimpleName(), EYBCardBadge.Special);

    public Geryuganshoop()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0,2, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        String message = Utilities.Format(cardData.strings.EXTENDED_DESCRIPTION[0], magicNumber);

        GameActionsHelper.CycleCardAction(this.secondaryValue);
        GameActionsHelper.AddToBottom(new ChooseAnyNumberFromPileAction(magicNumber, p.exhaustPile, this::OnCardChosen, this, message, true));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeMagicNumber(1);
        }
    }

    private void OnCardChosen(Object state, ArrayList<AbstractCard> cards)
    {
        boolean limited = PlayerStatistics.HasActivatedLimited(this.cardID);

        if (state == this && cards != null && cards.size() > 0)
        {
            for (AbstractCard card : cards)
            {
                AbstractDungeon.player.exhaustPile.removeCard(card);

                if (!limited && (card.cardID.equals(Boros.ID) || card.cardID.startsWith(Melzalgald.ID)))
                {
                    PlayerStatistics.TryActivateLimited(this.cardID);
                    GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(card, 1, true, true));
                }
                else
                {
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
                }
            }

            GameActionsHelper.GainEnergy(cards.size());
        }
    }
}