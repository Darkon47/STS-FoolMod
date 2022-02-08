package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.pcl.SwirledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Kazuha extends FoolCard
{
    public static final PCLCardData DATA = Register(Kazuha.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing).SetSeriesFromClassPackage(true);

    public Kazuha()
    {
        super(DATA);

        Initialize(7, 0, 3, 0);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Green, 9);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                .SetDamageEffect(c -> PCLGameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 1.4f, 1.6f).duration));

        int poisonAmount = PCLGameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID);
        int burningAmount = PCLGameUtilities.GetPowerAmount(m, BurningPower.POWER_ID);

        if (poisonAmount > 0) {
            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                if (mo != m) {
                    PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(mo), poisonAmount);
                }
            }
        }
        if (burningAmount > 0) {
            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                if (mo != m) {
                    PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(mo), burningAmount);
                }
            }
        }

        if (upgraded) {
            PCLActions.Bottom.StackPower(player, new SwirledPower(m, magicNumber));
        }

        if (PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Green) > 0 && TrySpendAffinity(PCLAffinity.Green)) {
            PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Green, 1);
        }
    }
}

