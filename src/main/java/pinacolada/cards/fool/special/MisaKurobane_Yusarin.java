package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.colorless.MisaKurobane;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class MisaKurobane_Yusarin extends FoolCard
{
    public static final PCLCardData DATA = Register(MisaKurobane_Yusarin.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColorless()
            .SetSeries(MisaKurobane.DATA.Series);

    public MisaKurobane_Yusarin()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TriggerOrbPassive(p.orbs.size())
                .SetFilter(o -> Fire.ORB_ID.equals(o.ID))
                .SetSequential(true);
        PCLActions.Bottom.Motivate(magicNumber);
    }
}