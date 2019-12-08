package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Add extends AnimatorCard
{
    public static final String ID = Register(Add.class.getSimpleName(), EYBCardBadge.Synergy);

    public Add()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 12);

        SetExhaust(true);
        SetEthereal(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.ExhaustFromPile(name, 1, p.hand, p.drawPile, p.discardPile)
        .AddCallback(this::OnCardChosen);

        if (HasActiveSynergy())
        {
            GameActionsHelper2.AddToBottom(new DoubleEnergyAction());
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetEthereal(false);
        }
    }

    private void OnCardChosen(ArrayList<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            AbstractCard c = cards.get(0);
            AbstractPlayer p = AbstractDungeon.player;

            CardGroup cardGroup = null;
            if (p.hand.contains(c))
            {
                cardGroup = p.hand;
            }
            else if (p.drawPile.contains(c))
            {
                cardGroup = p.drawPile;
            }
            else if (p.discardPile.contains(c))
            {
                cardGroup = p.discardPile;
            }

            if (cardGroup != null)
            {
                GameActionsHelper.AddToBottom(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(cardGroup, this::OrbChosen));
            }
        }
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        CardGroup cardGroup = JavaUtilities.SafeCast(state, CardGroup.class);
        if (cardGroup != null && chosen != null && chosen.size() == 1)
        {
            switch (cardGroup.type)
            {
                case DRAW_PILE:
                    GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(chosen.get(0), 1, true, true));
                    break;

                case HAND:
                    GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(chosen.get(0), 1));
                    break;

                case DISCARD_PILE:
                    GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(chosen.get(0), 1));
                    break;

                case MASTER_DECK:
                    break;
                case EXHAUST_PILE:
                    break;
                case CARD_POOL:
                    break;
                case UNSPECIFIED:
                    break;
            }
        }
    }
}