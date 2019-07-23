package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Ara extends AnimatorCard
{
    public static final String ID = CreateFullID(Ara.class.getSimpleName());

    public Ara()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        int debuffs = 0;
        for (AbstractPower power : m.powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                debuffs += 1;
            }
        }

        GameActionsHelper.DrawCard(p, debuffs);
        GameActionsHelper.Discard(1, false);
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