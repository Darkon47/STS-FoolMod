package eatyourbeets.cards.animator.series.OwariNoSeraph;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Mitsuba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mitsuba.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Mitsuba()
    {
        super(DATA);

        Initialize(7, 2, 2, 6);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Orange(2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && enemy.currentHealth > player.currentHealth)
        {
            return super.ModifyBlock(enemy, amount + secondaryValue);
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
    }
}