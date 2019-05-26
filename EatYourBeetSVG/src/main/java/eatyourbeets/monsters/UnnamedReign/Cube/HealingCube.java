package eatyourbeets.monsters.UnnamedReign.Cube;

import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingCubePower;

public class HealingCube extends Cube
{
    public HealingCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.add(new Move_GuardedAttack(0, 18, 16, this));
    }

    @Override
    protected void getMove(int i)
    {
        moveset.get(0).SetMove();
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 4;
                break;

            case Normal:
                amount = 5;
                break;

            case Advanced:
                amount = 6;
                break;

            case Ultimate:
                amount = 7;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new HealingCubePower(this, amount), amount);
    }
}
