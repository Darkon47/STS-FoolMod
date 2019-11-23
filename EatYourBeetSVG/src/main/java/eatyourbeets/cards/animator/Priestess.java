package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class Priestess extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Priestess.class.getSimpleName(), EYBCardBadge.Synergy);

    public Priestess()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0, 3, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, secondaryValue, false), secondaryValue);
        GameActionsHelper.GainTemporaryHP(p, magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);
            GameActionsHelper.DelayedAction(this);
            GameActionsHelper.ResetOrder();
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }

    private boolean TryExhaust(CardGroup source)
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : source.group)
        {
            if (GameUtilities.IsCurseOrStatus(c))
            {
                cards.add(c);
            }
        }

        if (cards.size() > 0)
        {
            GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(JavaUtilities.GetRandomElement(cards), source));

            return true;
        }

        return false;
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (!TryExhaust(p.drawPile))
            {
                if (!TryExhaust(p.hand))
                {
                    TryExhaust(p.discardPile);
                }
            }
        }
    }
}