package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateWispPower;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        boolean asc4 = PlayerStatistics.GetActualAscensionLevel() >= 4;

        movesetMode = Mode.Sequential;
        moveset.AddNormal(new Move_AttackMultiple( 8,3));
        moveset.AddNormal(new Move_GainStrengthAndArtifactAll( 3, 2));
        moveset.AddNormal(new Move_AttackMultipleFrail(2,8, asc4 ? 3 : 2));
        moveset.AddNormal(new Move_AttackMultipleHex( 6,4, 1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new UltimateWispPower(this));
    }
}