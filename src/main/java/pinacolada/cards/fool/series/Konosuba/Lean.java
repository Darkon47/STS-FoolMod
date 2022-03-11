package pinacolada.cards.fool.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Lean extends FoolCard
{
    public static final PCLCardData DATA = Register(Lean.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetMultiformData(2, false)
            .SetTraits(PCLCardTrait.Spellcaster)
            .SetSeriesFromClassPackage();

    public Lean()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
        SetAffinityRequirement(PCLAffinity.Green, 4);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                SetRetain(true);
            }
            else {
                SetHaste(true);

            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLJUtils.Count(player.hand.group, PCLGameUtilities::HasBlueAffinity);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        int amount = GetXValue();
        if (amount > 0) {
            PCLActions.Bottom.GainSupportDamage(amount);
        }

        if (TrySpendAffinity(PCLAffinity.Blue)) {
            PCLActions.Bottom.ChannelOrb(PCLOrbHelper.RandomCommonOrb());
        }
        if (TrySpendAffinity(PCLAffinity.Green)) {
            PCLActions.Bottom.ChannelOrb(PCLOrbHelper.RandomCommonOrb());
        }
    }
}