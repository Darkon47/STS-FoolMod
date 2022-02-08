package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Earth;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class ElricEdward extends PCLCard
{
    public static final PCLCardData DATA = Register(ElricEdward.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Fire)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public ElricEdward()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(3, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetEvokeOrbCount(1);
        SetProtagonist(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(5, 0, 1);
            SetUpgrade(2, 0);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(upgraded && auxiliaryData.form == 1 ? cardData.Strings.EXTENDED_DESCRIPTION[0] : "");
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Cycle(name, 1).AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                switch (c.type)
                {
                    case ATTACK:
                        PCLActions.Bottom.ChannelOrb(new Lightning());
                        return;

                    case SKILL:
                        PCLActions.Bottom.ChannelOrb(new Frost());
                        return;

                    case POWER:
                        PCLActions.Bottom.ChannelOrb(new Earth());
                        return;

                    case CURSE:
                    case STATUS:
                        if (upgraded && auxiliaryData.form == 1)
                        {
                            PCLActions.Bottom.ChannelOrb(new Fire());
                        }
                        return;
                }
            }
        });
    }
}