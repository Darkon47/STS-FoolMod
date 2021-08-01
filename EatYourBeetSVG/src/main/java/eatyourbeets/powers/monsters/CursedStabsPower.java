package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class CursedStabsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CursedStabsPower.class);

    public CursedStabsPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1, (owner == player) ? PowerType.DEBUFF : PowerType.BUFF, false);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        enabled = true;
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS && enabled)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Pain());
            flashWithoutSound();
            enabled = false;
        }
    }
}