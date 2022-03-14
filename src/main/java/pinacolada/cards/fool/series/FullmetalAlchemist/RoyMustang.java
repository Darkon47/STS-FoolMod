package pinacolada.cards.fool.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class RoyMustang extends FoolCard
{
    public static final PCLCardData DATA = Register(RoyMustang.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Fire, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();
    public static final int BURNING_ATTACK_BONUS = 15;
    public static final int BASE_BURNING = 5;

    public RoyMustang()
    {
        super(DATA);

        Initialize(4, 0, BASE_BURNING, BURNING_ATTACK_BONUS);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetEvokeOrbCount(1);

        SetAffinityRequirement(PCLAffinity.Red, 5);
    }

    @Override
    public int GetXValue() {
        return Math.max(1,BASE_BURNING - Math.max(0, PCLGameUtilities.GetEnemies(true).size() - 1));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.FIRE);
        PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), GetXValue());

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.AddPowerEffectEnemyBonus(BurningPower.POWER_ID, BURNING_ATTACK_BONUS);
        }
    }
}