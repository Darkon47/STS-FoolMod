package eatyourbeets.monsters.UnnamedReign.Cultist;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleCard;
import eatyourbeets.monsters.SharedMoveset.Move_Talk;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_SummonEnemy;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.TheUnnamedCultistPower;

public class TheUnnamed_Cultist_DollSummoner extends TheUnnamed_Cultist
{
    private Move_SummonEnemy moveSummonEnemy;

    public TheUnnamed_Cultist_DollSummoner(float x, float y)
    {
        super(x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddSpecial(new Move_Talk());

        moveSummonEnemy = (Move_SummonEnemy)
        moveset.AddSpecial(new Move_SummonEnemy());

        moveset.AddNormal(new Move_AttackDefend( 12, 12));
        moveset.AddNormal(new Move_AttackMultiple(9, 2));
        moveset.AddNormal(new Move_ShuffleCard(new VoidCard(), 2));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new TheUnnamedCultistPower(this, 10), 10);
        GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.2f));
        GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[5], 1f, 2.5f));
    }

    @Override
    public void die()
    {
        super.die();

        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.hasPower(MinionPower.POWER_ID))
            {
                GameActionsHelper.AddToBottom(new EscapeAction(m));
            }
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (moveSummonEnemy.CanUse(previousMove))
        {
            Move_Talk moveTalk = moveset.GetMove(Move_Talk.class);
            if (historySize == 0)
            {
                moveTalk.SetLine(data.strings.DIALOG[6]);
                moveTalk.SetMove();
            }
            else if (historySize == 1)
            {
                moveTalk.SetLine(data.strings.DIALOG[8]);
                moveTalk.SetMove();
            }
            else
            {
                moveSummonEnemy.SetSummon(new TheUnnamed_Doll(null, -40, 135));
                moveSummonEnemy.SetMove();
            }

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }
}
