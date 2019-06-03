package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_Defend;
import eatyourbeets.monsters.SharedMoveset.Move_GainTempThorns;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingCubePower;

public class HealingCube extends Cube
{
    public HealingCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_GainTempThorns(tier.Add(3,1)));
        moveset.AddNormal(new Move_Attack(tier.Add(6,2)));
        moveset.AddNormal(new Move_Defend(tier.Add(6,2)));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 3;
                break;

            case Normal:
                amount = 4;
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
