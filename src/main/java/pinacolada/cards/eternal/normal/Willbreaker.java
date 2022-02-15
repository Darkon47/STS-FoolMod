package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Willbreaker extends EternalCard
{
    public static final PCLCardData DATA = Register(Willbreaker.class)
            .SetMaxCopies(2)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Normal);

    public Willbreaker()
    {
        super(DATA);

        Initialize(15, 0, 12, 4);
        SetUpgrade(3, 0, 0, 1);

        SetDark();
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        if (PCLCombatStats.MatchingSystem.ResolveMeter.TrySpendResolve(magicNumber)) {
            PCLActions.Bottom.ReduceStrength(m, secondaryValue, false).SetStrengthGain(CheckPrimaryCondition(true) && info.TryActivateLimited());
        }
    }
}