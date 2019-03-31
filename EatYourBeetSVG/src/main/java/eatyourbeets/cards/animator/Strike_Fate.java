package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class Strike_Fate extends Strike
{
    public static final String ID = CreateFullID(Strike_Fate.class.getSimpleName());

    public Strike_Fate()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7,0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
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