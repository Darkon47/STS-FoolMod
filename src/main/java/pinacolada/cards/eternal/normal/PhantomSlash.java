package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class PhantomSlash extends EternalCard
{
    public static final PCLCardData DATA = Register(PhantomSlash.class).SetAttack(0, CardRarity.COMMON, PCLAttackType.Piercing);

    public PhantomSlash()
    {
        super(DATA);

        Initialize(1, 0, 8, 5);
        SetUpgrade(1, 0, 2);
        SetHitCount(2);

        SetDark();
        SetEthereal(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (CheckPrimaryCondition(true)) {
            amount += magicNumber;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.Draw(1);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.Draw(1);
        }
    }

}