package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorCard
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
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) 
    {
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.StackPower(new PoisonAffinityPower(p, 1));
    }
}