package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class CursedStabsPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(CursedStabsPower.class);
    public int usesThisTurn = 99;

    public CursedStabsPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        usesThisTurn = 99;
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            if (usesThisTurn > 0)
            {
                GameActions.Bottom.MakeCardInDrawPile(new Pain());

                usesThisTurn -= 1;
            }
        }
    }
}