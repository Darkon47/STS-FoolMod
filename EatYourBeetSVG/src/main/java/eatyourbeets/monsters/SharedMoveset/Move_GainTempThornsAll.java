package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainTempThornsAll extends AbstractMove
{
    private final int buffAmount;

    public Move_GainTempThornsAll(int buffAmount)
    {
        this.buffAmount = buffAmount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyPower(owner, m, new EarthenThornsPower(m, buffAmount), buffAmount);
        }
    }
}
