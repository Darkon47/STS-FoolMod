package eatyourbeets.monsters.UnnamedReign.UnnamedCultist.Moveset;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.SummonMonsterAction;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;

public class Move_SummonEnemy extends AbstractMove
{
    private int summonCount = 0;
    private TheUnnamed_Cultist cultist;
    private AbstractMonster summon;

    @Override
    public void Init(byte id, AbstractMonster owner)
    {
        super.Init(id, owner);

        cultist = (TheUnnamed_Cultist)owner;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return (summonCount == 0 || !(summon instanceof TheUnnamed_Doll));
    }

    public void SetSummon(AbstractMonster monster)
    {
        summon = monster;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        summonCount += 1;

        if (summon.id.equals(TheUnnamed_Doll.ID))
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.data.strings.DIALOG[1], 0.5f, 2f));
            GameActionsHelper.AddToBottom(new WaitRealtimeAction(2f));
            GameActionsHelper.AddToBottom(new SummonMonsterAction(summon, true));
            return;
        }

        if (summon.id.equals(ShelledParasite.ID))
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.data.strings.DIALOG[4], 3f, 3f));
        }
        else if (summon.id.equals(GremlinWarrior.ID))
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.data.strings.DIALOG[3], 3f, 3f));
        }
        else
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, cultist.data.strings.DIALOG[2].replace("@", summon.name), 3f, 3f));
        }

        GameActionsHelper.AddToBottom(new SummonMonsterAction(summon, false));
    }
}
