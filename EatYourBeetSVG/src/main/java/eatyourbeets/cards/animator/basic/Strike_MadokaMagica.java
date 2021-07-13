package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Strike_MadokaMagica extends Strike
{
    public static final String ID = Register(Strike_MadokaMagica.class).ID;

    public Strike_MadokaMagica()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0, 2);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.MadokaMagica);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.Scry(magicNumber);
    }
}