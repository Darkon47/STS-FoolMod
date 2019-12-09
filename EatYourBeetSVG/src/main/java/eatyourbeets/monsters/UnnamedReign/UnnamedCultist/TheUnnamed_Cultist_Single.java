package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_GainPlatedArmorAll;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAll;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.Moveset.Move_SummonEnemy;
import eatyourbeets.powers.UnnamedReign.TheUnnamedCultistPower;

public class TheUnnamed_Cultist_Single extends TheUnnamed_Cultist
{
    public final RandomizedList<AbstractMonster> pool1 = new RandomizedList<>();
    public final RandomizedList<AbstractMonster> pool2 = new RandomizedList<>();
    public AbstractMonster firstSummon;
    public AbstractMonster secondSummon;

    public TheUnnamed_Cultist_Single(float x, float y)
    {
        super(x, y);

        PreparePool(GameUtilities.GetAscensionLevel());

        moveset.AddSpecial(new Move_SummonEnemy());

        boolean asc4 = GameUtilities.GetAscensionLevel() >= 4;

        int strengthGain = asc4 ? 4 : 3;
        int platedArmor = asc4 ? 5 : 4;

        moveset.AddNormal(new Move_GainPlatedArmorAll(platedArmor));
        moveset.AddNormal(new Move_GainStrengthAll(strengthGain));
        moveset.AddNormal(new Move_AttackDefend(12, 12));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper_Legacy.ApplyPower(this, this, new TheUnnamedCultistPower(this, 15), 15);
        GameActionsHelper_Legacy.AddToBottom(new TalkAction(this, data.strings.DIALOG[10], 1f, 3f));
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (historySize % 3 == 0 && TrySummon())
        {
            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }

    private boolean TrySummon()
    {
        if (firstSummon == null || firstSummon.isDeadOrEscaped())
        {
            firstSummon = pool1.Retrieve(AbstractDungeon.aiRng);
            if (firstSummon != null)
            {
                Move_SummonEnemy moveSummonEnemy = moveset.GetMove(Move_SummonEnemy.class);
                moveSummonEnemy.SetSummon(firstSummon);
                moveSummonEnemy.SetMove();
                return true;
            }
        }

        if (secondSummon == null || secondSummon.isDeadOrEscaped())
        {
            secondSummon = pool2.Retrieve(AbstractDungeon.aiRng);
            if (secondSummon != null)
            {
                Move_SummonEnemy moveSummonEnemy = moveset.GetMove(Move_SummonEnemy.class);
                moveSummonEnemy.SetSummon(secondSummon);
                moveSummonEnemy.SetMove();
                return true;
            }
        }

        return false;
    }

    private void PreparePool(int level)
    {
        AbstractMonster m;
        m = new Byrd(-80, 24);
        m.currentHealth = m.maxHealth += 24 + level / 2;
        pool1.Add(m);

        m = new Sentry(-80, 24);
        m.currentHealth = m.maxHealth += 28 + level / 2;
        pool1.Add(m);

        m = new GremlinWarrior(-80, 24);
        m.currentHealth = m.maxHealth += 34 + level / 2;
        pool1.Add(m);

        m = new OrbWalker(-330, -26);
        m.currentHealth = m.maxHealth += 20 + level / 2;
        pool2.Add(m);

        m = new ShelledParasite(-352, -26);
        m.currentHealth = m.maxHealth += 20 + level / 2;
        pool2.Add(m);
    }
}