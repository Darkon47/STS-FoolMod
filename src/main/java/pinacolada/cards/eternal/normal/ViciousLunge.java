package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.Mathf;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class ViciousLunge extends EternalCard
{
    public static final PCLCardData DATA = Register(ViciousLunge.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing);

    public ViciousLunge()
    {
        super(DATA);

        Initialize(7, 0, 6, 40);
        SetUpgrade(2, 0);
        SetHitCount(2);

        SetDark();
        SetEthereal(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (CheckPrimaryCondition(true)) {
            amount *= 1.4f;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public int GetXValue() {
        return CheckPrimaryCondition(true) ? Mathf.CeilToInt(magicNumber * 1.4f) : magicNumber;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        if (IsMismatching(info)) {
            PCLActions.Bottom.TakeDamage(GetXValue(), AttackEffects.BLUNT_HEAVY);
        }
    }
}