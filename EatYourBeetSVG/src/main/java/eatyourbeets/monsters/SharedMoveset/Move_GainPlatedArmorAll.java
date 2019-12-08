package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainPlatedArmorAll extends AbstractMove
{
    private final int buffAmount;

    public Move_GainPlatedArmorAll(int buffAmount)
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
            GameActionsHelper.ApplyPower(owner, m, new PlatedArmorPower(m, buffAmount), buffAmount);
        }
    }
}
