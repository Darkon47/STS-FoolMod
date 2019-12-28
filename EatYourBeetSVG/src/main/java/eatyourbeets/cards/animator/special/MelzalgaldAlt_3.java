package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;

public class MelzalgaldAlt_3 extends MelzalgaldAlt
{
    public static final String ID = Register(MelzalgaldAlt_3.class);

    public MelzalgaldAlt_3()
    {
        super(ID);

        Initialize(7, 0, 2);
        SetUpgrade(0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainAgility(magicNumber);
    }
}