package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class CursedBlade extends AnimatorRelic
{
    private static final int BUFF_AMOUNT = 3;

    public static final String ID = CreateFullID(CursedBlade.class);

    public CursedBlade()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], BUFF_AMOUNT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.GainForce(BUFF_AMOUNT);
        GameActions.Bottom.GainAgility(BUFF_AMOUNT);
        this.flash();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > player.currentBlock)
        {
            GameActions.Bottom.MakeCardInHand(new Wound());
            this.flash();
        }

        return super.onAttacked(info, damageAmount);
    }
}