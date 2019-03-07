package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;


public class AinzEffect_DamageAll extends AinzEffect
{
    public AinzEffect_DamageAll(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseDamage = ainz.damage = upgraded ? 20 : 14;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.DamageTarget(p, m, ainz.damage, ainz.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        }
    }
}