package pinacolada.cards.fool.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.pcl.Earth;
import pinacolada.orbs.pcl.Fire;
import pinacolada.stances.pcl.*;
import pinacolada.utilities.PCLActions;

public class Shimako extends FoolCard
{
    public static final PCLCardData DATA = Register(Shimako.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    public Shimako()
    {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
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

        AbstractOrb orb;
        String curStance = player.stance.ID;
        if (curStance.equals(MightStance.STANCE_ID) || curStance.equals(InvocationStance.STANCE_ID))
        {
            orb = new Fire();
        }
        else if (curStance.equals(EnduranceStance.STANCE_ID))
        {
            orb = new Earth();
        }
        else if (curStance.equals(WisdomStance.STANCE_ID) || curStance.equals(DesecrationStance.STANCE_ID))
        {
            orb = new Dark();
        }
        else
        {
            orb = new Lightning();
        }

        PCLActions.Bottom.ChannelOrb(orb);

        if (curStance.equals(WisdomStance.STANCE_ID))
        {
            PCLActions.Bottom.ChannelOrb(new Dark());
        }
    }
}