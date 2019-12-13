package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.HexPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_Hexed extends AbstractMove
{
    public final int amount;

    public Move_Hexed(int amount)
    {
        this.amount = amount;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.StackPower(owner, new HexPower(target, amount));
    }
}