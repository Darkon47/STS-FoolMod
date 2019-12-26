package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.RoseDamageAction;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class Rose extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Rose.class.getSimpleName(), EYBCardBadge.Special);

    public Rose()
    {
        super(ID, 3, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(8, 0, 2, 40);
        SetUpgrade(0, 0, 1, 0);

        SetPiercing(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(this.magicNumber);
        GameActions.Bottom.Reload(name, m, (enemy, cards) ->
        {
            AbstractMonster monster = JavaUtilities.SafeCast(enemy, AbstractMonster.class);
            if (cards.size() > 0 && monster != null && !GameUtilities.IsDeadOrEscaped(monster))
            {
                GameActions.Bottom.Add(new RoseDamageAction(monster, this, cards.size(), damage));
            }
        });
    }
}