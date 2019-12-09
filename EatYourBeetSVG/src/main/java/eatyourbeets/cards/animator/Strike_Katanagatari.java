package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class Strike_Katanagatari extends Strike
{
    public static final String ID = Register(Strike_Katanagatari.class.getSimpleName());

    public Strike_Katanagatari()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT).SetOptions(true, true);
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
