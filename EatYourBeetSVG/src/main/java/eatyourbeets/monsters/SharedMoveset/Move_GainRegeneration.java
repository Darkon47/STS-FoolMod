package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_GainRegeneration extends AbstractMove
{
    private final int amount;

    public Move_GainRegeneration(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.ApplyPower(owner, owner, new RegenPower(owner, amount), amount);
    }
}
