package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.orbs.Aether;

public class AirOrbEvokeAction extends AbstractGameAction
{
    private final Aether wind;

    public AirOrbEvokeAction(Aether wind)
    {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.wind = wind;
    }

    public void update()
    {
        GameActionsHelper_Legacy.DrawCard(AbstractDungeon.player, wind.evokeAmount);

        this.isDone = true;
    }
}