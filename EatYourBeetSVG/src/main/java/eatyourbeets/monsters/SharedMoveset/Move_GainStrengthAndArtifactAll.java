package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainStrengthAndArtifactAll extends AbstractMove
{
    private final int strength;
    private final int artifact;

    public Move_GainStrengthAndArtifactAll(int strength, int artifact)
    {
        this.strength = strength;
        this.artifact = artifact;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.BUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyPower(owner, m, new StrengthPower(m, strength), strength);
            GameActions.Bottom.ApplyPower(owner, m, new ArtifactPower(m, artifact), artifact);
        }
    }
}
