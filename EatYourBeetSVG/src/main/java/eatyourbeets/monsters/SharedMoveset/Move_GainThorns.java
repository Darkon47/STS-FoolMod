package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_GainThorns extends AbstractMove
{
    private final int amount;

    public Move_GainThorns(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.StackPower(new ThornsPower(owner, amount));
    }
}