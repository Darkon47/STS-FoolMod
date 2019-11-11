package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Strike_Konosuba extends Strike
{
    public static final String ID = Register(Strike_Konosuba.class.getSimpleName());

    public Strike_Konosuba()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,2);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.GainBlock(p, block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}