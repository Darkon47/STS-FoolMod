package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class RoryMercury extends AnimatorCard
{
    public static final String ID = Register(RoryMercury.class.getSimpleName(), EYBCardBadge.Drawn);

    public RoryMercury()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(3,0, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainForce(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}