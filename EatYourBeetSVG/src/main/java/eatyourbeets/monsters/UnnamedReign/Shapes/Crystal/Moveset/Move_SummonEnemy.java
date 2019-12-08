package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions._legacy.common.SummonMonsterAction;
import eatyourbeets.monsters.AbstractMove;

public class Move_SummonEnemy extends AbstractMove
{
    private int summonCount = 0;
    private AbstractMonster summon;

    @Override
    public void Init(byte id, AbstractMonster owner)
    {
        super.Init(id, owner);
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return summonCount == 0;
    }

    public void SetSummon(AbstractMonster monster)
    {
        summon = monster;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        summonCount += 1;

        GameActionsHelper.AddToBottom(new SummonMonsterAction(summon, false));
    }
}
