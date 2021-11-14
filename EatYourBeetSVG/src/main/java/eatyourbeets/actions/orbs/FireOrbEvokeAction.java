package eatyourbeets.actions.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.FireballEffect2;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class FireOrbEvokeAction extends EYBAction
{
    protected final Fire orb;

    public FireOrbEvokeAction(Fire orb, int damage)
    {
        super(ActionType.DEBUFF);

        this.orb = orb;

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        int maxHealth = Integer.MIN_VALUE;
        AbstractMonster enemy = null;

        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            if (m.currentHealth > maxHealth)
            {
                maxHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            GameActions.Top.ApplyBurning(source, enemy, MathUtils.ceil(amount / 2f));

            int actualDamage = AbstractOrb.applyLockOn(enemy, amount);
            if (actualDamage > 0)
            {
                GameActions.Top.DealDamage(source, enemy, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.FIRE)
                        .SetVFX(true, true);
            }

            GameActions.Top.Wait(0.15f);
            GameActions.Top.VFX(new FireballEffect2(orb.hb.cX, orb.hb.cY, enemy.hb.cX, enemy.hb.cY)
                    .SetColor(Color.RED, Color.FIREBRICK).SetRealtime(true));
        }

        Complete();
    }
}
