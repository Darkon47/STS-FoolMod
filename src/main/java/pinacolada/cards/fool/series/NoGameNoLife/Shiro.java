package pinacolada.cards.fool.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Shiro extends FoolCard
{
    public static final PCLCardData DATA = Register(Shiro.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None, false)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Protagonist);
    public static final int CHARGE_COST = 8;

    public Shiro()
    {
        super(DATA);

        Initialize(0, 1, 4, 3);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(CHARGE_COST);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.Scry(secondaryValue).AddCallback(
                cards -> {
                    if (cards.size() > 0) {
                        PCLActions.Bottom.TriggerOrbPassive(cards.size(), true, false);
                    }

                    if (PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Blue) > 0 && PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Light, CHARGE_COST)) {
                        PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Blue, PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Blue));
                    }
                }
        );
    }
}