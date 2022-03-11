package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class UryuuIshida extends FoolCard {
    public static final PCLCardData DATA = Register(UryuuIshida.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public UryuuIshida() {
        super(DATA);

        Initialize(4, 1, 1, 1);
        SetUpgrade(2, 1, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Silver(1, 0 ,0);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    public void triggerOnManualDiscard() {
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.GainSupportDamage(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (IsStarter()) {
            PCLActions.Bottom.Callback(m, (enemy, __) -> TransferWeakVulnerable(enemy));
        }

        if (info.IsSynergizing && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.GainSupportDamage(secondaryValue);
        }
    }

    private void TransferWeakVulnerable(AbstractMonster m) {
        int weakToTransfer = PCLGameUtilities.GetPowerAmount(player, WeakPower.POWER_ID);
        int vulToTransfer = PCLGameUtilities.GetPowerAmount(player, VulnerablePower.POWER_ID);

        if (weakToTransfer > magicNumber) {
            weakToTransfer = magicNumber;
        }
        if (vulToTransfer > magicNumber) {
            vulToTransfer = magicNumber;
        }

        for (AbstractPower power : player.powers) {
            if (WeakPower.POWER_ID.equals(power.ID) && weakToTransfer > 0) {
                PCLActions.Bottom.ReducePower(power, weakToTransfer);
                PCLActions.Bottom.ApplyWeak(player, m, weakToTransfer);
            } else if (VulnerablePower.POWER_ID.equals(power.ID) && vulToTransfer > 0) {
                PCLActions.Bottom.ReducePower(power, vulToTransfer);
                PCLActions.Bottom.ApplyVulnerable(player, m, vulToTransfer);
            }
        }
    }
}