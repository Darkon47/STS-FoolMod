package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.DrawCards;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.StolenGoldPower;
import eatyourbeets.utilities.GameUtilities;

public class Chris extends AnimatorCard
{
    public static final String ID = Register(Chris.class.getSimpleName(), EYBCardBadge.Discard);

    public Chris()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4,0, 4);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper2.AddToTop(new DrawCards(1)
        .SetFilter(card -> card.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(card), false));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper2.ApplyPowerSilently(p, m, new StolenGoldPower(m, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }
}