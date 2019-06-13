package eatyourbeets.monsters.SharedMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

public class Move_AttackVulnerable extends AbstractMove
{
    private final int debuffAmount;

    public Move_AttackVulnerable(int damageAmount, int debuffAmount)
    {
        this.debuffAmount = debuffAmount;
        damageInfo = new DamageInfo(owner, damageAmount + GetBonus(damageAmount, 0.2f));
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK_DEBUFF, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        owner.useFastAttackAnimation();
        damageInfo.applyPowers(owner, target);
        GameActionsHelper.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.FIRE));
        GameActionsHelper.ApplyPower(owner, target, new VulnerablePower(target, debuffAmount, true), debuffAmount);
    }
}