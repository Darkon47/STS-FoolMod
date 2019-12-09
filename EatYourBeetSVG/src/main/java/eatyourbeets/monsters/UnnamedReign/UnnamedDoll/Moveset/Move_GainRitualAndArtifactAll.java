package eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameUtilities;

public class Move_GainRitualAndArtifactAll extends AbstractMove
{
    private final int ritual;
    private final int artifact;

    public Move_GainRitualAndArtifactAll(int ritual, int artifact)
    {
        this.ritual = ritual;
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
            GameActionsHelper_Legacy.ApplyPower(owner, m, new RitualPower(m, ritual), ritual);
            GameActionsHelper_Legacy.ApplyPower(owner, m, new ArtifactPower(m, artifact), artifact);
        }
    }
}