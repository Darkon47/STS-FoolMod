package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class ThrowingKnife_2 extends ThrowingKnife
{
    public static final String ID = Register(ThrowingKnife_2.class.getSimpleName(), EYBCardBadge.Discard);

    public ThrowingKnife_2()
    {
        super(ID);

        Initialize(2,0, 2);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ApplyPoison(p, m, magicNumber);
    }
}