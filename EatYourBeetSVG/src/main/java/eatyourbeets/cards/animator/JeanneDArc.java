package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;

import java.util.ArrayList;

public class JeanneDArc extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(JeanneDArc.class.getSimpleName());

    public JeanneDArc()
    {
        super(ID, 1, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(10,0, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, this.magicNumber), this.magicNumber);

        ArrayList<AbstractCard> cards = new ArrayList<>();
        if (!TryExhaust(p.hand))
        {
            if (!TryExhaust(p.drawPile))
            {
                TryExhaust(p.discardPile);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

    private boolean TryExhaust(CardGroup source)
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : source.group)
        {
            if (c.type == CardType.CURSE || c.type == CardType.STATUS)
            {
                cards.add(c);
            }
        }

        if (cards.size() > 0)
        {
            GameActionsHelper.ExhaustCard(Utilities.GetRandomElement(cards), source);

            return true;
        }

        return false;
    }
}