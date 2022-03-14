package pinacolada.cards.fool.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MikuIzayoi extends FoolCard
{
    public static final PCLCardData DATA = Register(MikuIzayoi.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None, true)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public MikuIzayoi()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetAffinity_Light(1, 0, 1);
        SetEthereal(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetEthereal(true);
                SetUpgrade(0, 0, 3, 0);
            }
            else {
                SetEthereal(false);
                SetUpgrade(0, 0, 0, 0);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.GainEnergyNextTurn(1);
        if (CheckSpecialCondition(true)) {
            PCLActions.Bottom.GainInspiration(1);
        }

        if (info.IsSynergizing && info.TryActivateSemiLimited()) {
            PCLCombatStats.MatchingSystem.AffinityMeter.IncreaseMatchCombo(secondaryValue);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        if (PCLGameUtilities.IsPCLAffinityPowerActive(PCLAffinity.Blue)) {
            return true;
        }
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (card.type == CardType.POWER)
            {
                return true;
            }
        }
        return false;
    }
}