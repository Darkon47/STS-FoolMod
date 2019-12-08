package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.AbstractMove;

public class Move_Talk extends AbstractMove
{
    private String line;

    public void SetMove()
    {
        this.owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new TalkAction(owner, line));
    }

    public void SetLine(String line)
    {
        this.line = line;
    }
}
