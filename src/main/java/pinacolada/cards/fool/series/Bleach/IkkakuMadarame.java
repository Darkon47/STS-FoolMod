package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.IkkakuBankai;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IkkakuMadarame extends FoolCard {
    public static final PCLCardData DATA = Register(IkkakuMadarame.class).SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.AoE).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new IkkakuBankai(), false);
            });

    public IkkakuMadarame() {
        super(DATA);

        Initialize(11, 2, 2, 15);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);
        

        PCLGameUtilities.MaintainPower(PCLAffinity.Red);
        PCLGameUtilities.MaintainPower(PCLAffinity.Green);

        if (CheckSpecialCondition(true)) {
            PCLActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
            PCLActions.Last.ModifyAllInstances(uuid).AddCallback(PCLActions.Bottom::Exhaust);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Red) >= secondaryValue || PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Green) >= secondaryValue;
    }
}