package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.monsters.AbstractMove;

public class Move_MultiSlash extends AbstractMove
{
    private final int TIMES;

    private int timesCounter;

    public Move_MultiSlash()
    {
        damageInfo = new DamageInfo(owner, 3);
        TIMES = 4;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        if (super.CanUse(previousMove))
        {
            return GameUtilities.GetPowerAmount(owner, StrengthPower.POWER_ID) >= 3;
        }

        return false;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base, TIMES, true);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);
        timesCounter = TIMES;
        Attack(target);
    }

    private void Attack(AbstractPlayer target)
    {
        if (timesCounter > 0)
        {
            AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
            GameActions.Bottom.Callback(new DamageAction(target, damageInfo, attackEffect), target.currentHealth, (state, __) ->
            {
                Integer previousHealth = JavaUtilities.SafeCast(state, Integer.class);
                if (previousHealth != null)
                {
                    int difference = previousHealth - AbstractDungeon.player.currentHealth;
                    if (difference > 0)
                    {
                        GameActions.Bottom.Add(new HealAction(owner, owner, difference));
                    }

                    Attack(AbstractDungeon.player);
                }
            });

            timesCounter -= 1;
        }
    }
}