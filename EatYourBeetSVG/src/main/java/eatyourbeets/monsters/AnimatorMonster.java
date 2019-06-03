package eatyourbeets.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.AbstractMonsterData;

import java.util.ArrayList;

public abstract class AnimatorMonster extends CustomMonster
{
    public final Moveset moveset = new Moveset(this);
    public final AbstractMonsterData data;

    public AnimatorMonster(AbstractMonsterData data, EnemyType type)
    {
        this(data, type, 0, 0);
    }

    public AnimatorMonster(AbstractMonsterData data, EnemyType type, float x, float y)
    {
        super(data.strings.NAME, data.id, data.maxHealth, data.hb_x, data.hb_y, data.hb_w, data.hb_h,
                data.imgUrl, data.offsetX + x, data.offsetY + y);

        this.data = data;
        this.type = type;
    }

    protected void ExecuteNextMove()
    {
        moveset.GetMove(nextMove).Execute(AbstractDungeon.player);
    }

    @Override
    public void takeTurn()
    {
        ExecuteNextMove();

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }

    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        ArrayList<AbstractMove> moves = new ArrayList<>();
        for (AbstractMove move : moveset.rotation)
        {
            if (move.CanUse(previousMove))
            {
                moves.add(move);
            }
        }

        moves.get(roll % moves.size()).SetMove();
    }

    @Override
    protected void getMove(int i)
    {
        Byte previousMove = -1;
        int size = moveHistory.size();
        if (size > 0)
        {
            previousMove = moveHistory.get(size - 1);
        }

        SetNextMove(i, size, previousMove);
    }

    @Override
    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale)
    {
        super.loadAnimation(atlasUrl, skeletonUrl, scale);
    }
}
