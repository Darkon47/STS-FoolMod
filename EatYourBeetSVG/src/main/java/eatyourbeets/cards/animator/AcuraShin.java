package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class AcuraShin extends AnimatorCard
{
    public static final String ID = Register(AcuraShin.class.getSimpleName(), EYBCardBadge.Special);

    public AcuraShin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(2,0, 3);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.ApplyPoison(p, m, magicNumber);
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetOptions(true, true);
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetOptions(true, true);
        GameActionsHelper2.StackPower(new PoisonAffinityPower(p, 1));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}