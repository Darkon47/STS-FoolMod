package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackDefend extends AbstractMove
{
    private final int blockAmount;

    public Move_AttackDefend(int damageAmount, int blockAmount)
    {
        this.damageInfo = new DamageInfo(owner, damageAmount + CalculateAscensionBonus(damageAmount, 0.25f));
        this.blockAmount = blockAmount + CalculateAscensionBonus(blockAmount, 0.25f);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEFEND, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActions.Bottom.Add(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        GameActions.Bottom.GainBlock(owner, blockAmount);
    }
}