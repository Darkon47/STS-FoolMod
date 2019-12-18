package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import eatyourbeets.monsters.Moveset;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultipleFrail;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultipleVulnerable;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultipleWeak;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateCubePower;
import eatyourbeets.utilities.GameUtilities;

public class UltimateCube extends Cube
{
    public static final String ID = CreateFullID(MonsterShape.Cube, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Cube";

    public UltimateCube()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        moveset.mode = Moveset.Mode.Sequential;

        boolean asc4 = GameUtilities.GetActualAscensionLevel() >= 4;
        boolean asc18 = GameUtilities.GetActualAscensionLevel() >= 18;

        moveset.AddSpecial(new Move_AttackMultiple(6, asc4 ? 32 : 24));

        int debuffsAmount = asc18 ? 3 : 2;
        int damageAmount = asc4 ? 6 : 4;

        moveset.AddNormal(new Move_AttackMultipleWeak(damageAmount, 2, debuffsAmount));
        moveset.AddNormal(new Move_AttackMultipleVulnerable(damageAmount, 2, debuffsAmount));
        moveset.AddNormal(new Move_AttackMultipleFrail(damageAmount, 2, debuffsAmount));
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
        {
            moveset.GetMove(Move_AttackMultiple.class).SetMove();
        }
        else
        {
            super.SetNextMove(roll, historySize, previousMove);
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new UltimateCubePower(this, 12));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 3));
    }
}