package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.EnduranceStance;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GrimmjowJeagerjaques extends FoolCard
{
    public static final PCLCardData DATA = Register(GrimmjowJeagerjaques.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal).SetSeriesFromClassPackage(true);

    public GrimmjowJeagerjaques()
    {
        super(DATA);

        Initialize(12, 5, 5, 12);
        SetUpgrade(4, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return PCLGameUtilities.InBattle() && (MightStance.IsActive() || EnduranceStance.IsActive()) ? super.GetBlockInfo() : null;
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, PCLGameUtilities.GetPowerAmount(PCLAffinity.Green) >= magicNumber ? (amount + secondaryValue) : amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        if (PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Green, magicNumber)) {
            PCLActions.Bottom.GainVigor(magicNumber);
        }

        if (MightStance.IsActive() || EnduranceStance.IsActive()) {
            PCLActions.Bottom.GainBlock(block);
        }
    }
}