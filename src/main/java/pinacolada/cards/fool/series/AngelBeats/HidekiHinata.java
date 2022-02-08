package pinacolada.cards.fool.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HidekiHinata extends FoolCard
{
    public static final PCLCardData DATA = Register(HidekiHinata.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public HidekiHinata()
    {
        super(DATA);

        Initialize(6, 0, 2, 3);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(0,0,1);

        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + PCLGameUtilities.GetCurrentMatchCombo() * secondaryValue);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        if (PCLGameUtilities.GetCurrentAffinity() == PCLAffinity.Light || PCLGameUtilities.GetNextAffinity() == PCLAffinity.Light) {
            PCLActions.Bottom.GainSupportDamage(magicNumber);
        }
    }

    @Override
    public void triggerOnAfterlife() {
        PCLActions.Bottom.GainEnergyNextTurn(1);
    }
}