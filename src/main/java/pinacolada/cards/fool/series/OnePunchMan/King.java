package pinacolada.cards.fool.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class King extends FoolCard
{
    public static final PCLCardData DATA = Register(King.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false);

    public King()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 1, 0, 0);

        SetAffinity_Red(1, 0, 1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        SetInnate(upgraded && form == 0);
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(player), 1).IgnoreArtifact(true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Light, magicNumber)) {
            PCLActions.Bottom.GainMight(magicNumber);
        }
    }
}