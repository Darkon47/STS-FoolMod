package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCard_Status;

public class Status_Wound extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Status_Wound.class)
            .SetStatus(-2, CardRarity.COMMON, PCLCardTarget.None);

    public Status_Wound()
    {
        super(DATA);

        Initialize(0, 0);

        SetUnplayable(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        SetEthereal(form == 1);
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}