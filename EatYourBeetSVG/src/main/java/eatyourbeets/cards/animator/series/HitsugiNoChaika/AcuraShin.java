package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(AcuraShin.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeries(CardSeries.HitsugiNoChaika);

    public AcuraShin()
    {
        super(DATA);

        Initialize(3,0,2);
        SetCostUpgrade(-1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.StackPower(new PoisonAffinityPower(p, 1));
    }
}