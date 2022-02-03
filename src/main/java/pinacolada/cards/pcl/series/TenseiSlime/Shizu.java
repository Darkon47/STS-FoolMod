package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Shizu_Ifrit;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.special.BurningWeaponPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Shizu extends PCLCard
{
    public static final PCLCardData DATA = Register(Shizu.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Fire)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Shizu_Ifrit(), false));

    public Shizu()
    {
        super(DATA);

        Initialize(14, 0, 2, 2);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 6);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                Initialize(14, 0, 2, 2);
                SetUpgrade(4, 0);
                AddScaling(PCLAffinity.Red, 1);
            }
            else {
                Initialize(14, 0, 2, 2);
                SetUpgrade(0, 0, 2, 0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d
        .SetDamageEffect(c -> PCLGameEffects.List.Attack(player, c, AttackEffects.SLASH_DIAGONAL, 0.9f, 1.1f).duration));
        PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(m), 1);
        PCLActions.Bottom.StackPower(new BurningWeaponPower(p, magicNumber));

        if (CheckSpecialCondition(true) && TrySpendAffinity(PCLAffinity.Red))
        {
            this.exhaustOnUseOnce = true;
            PCLActions.Bottom.MakeCardInHand(new Shizu_Ifrit()).AddCallback(PCLGameUtilities::Retain);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return PCLGameUtilities.GetOrbCount(Dark.ORB_ID) >= 1 && PCLGameUtilities.GetOrbCount(Fire.ORB_ID) >= 1 && CheckAffinity(PCLAffinity.Red);
    }
}