package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Lean extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lean.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None, true)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Lean()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetAffinityRequirement(Affinity.Green, 3);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetHaste(true);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                Initialize(0, 0, 2, 2);
                SetUpgrade(0,0,3);
            }
            else {
                SetHaste(true);
                Initialize(0, 0, 2, 2);
                SetUpgrade(0,0,0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * JUtils.Count(player.hand.group, this::WouldSynergize);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.GainSupportDamage(GetXValue());
        if (TrySpendAffinity(Affinity.Blue)) {
            GameActions.Bottom.ChannelOrbs(GameUtilities::GetRandomCommonOrb, 1);
        }
        if (TrySpendAffinity(Affinity.Green)) {
            GameActions.Bottom.ChannelOrbs(GameUtilities::GetRandomCommonOrb, 1);
        }
    }
}