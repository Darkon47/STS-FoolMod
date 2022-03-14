package pinacolada.cards.fool.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.replacement.PCLVulnerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Mayuri extends FoolCard
{
    public static final PCLCardData DATA = Register(Mayuri.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Electric, PCLCardTarget.Random)
            .SetSeriesFromClassPackage();
    public static final int DAMAGE_THRESHOLD = 10;

    public Mayuri()
    {
        super(DATA);

        Initialize(4, 21, 2, 25);
        SetUpgrade(0, 3, 0, 5);
        SetAffinity_Light(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(DAMAGE_THRESHOLD);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.LIGHTNING);


        PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Player(), magicNumber).AddCallback(() -> {
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
                if (CheckSpecialCondition(true)) {
                    PCLActions.Bottom.AddPowerEffectEnemyBonus(ElectrifiedPower.POWER_ID, secondaryValue);
                    break;
                }
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        if (PCLGameUtilities.InBattle()) {
            float estimatedDifference = player.hasPower(VulnerablePower.POWER_ID) ? 1f : 1f + (PCLVulnerablePower.ATTACK_MULTIPLIER + PCLCombatStats.GetEffectBonus(VulnerablePower.POWER_ID)) / 100f;
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
                if (intent.GetDamage(true) * estimatedDifference >= DAMAGE_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }
}