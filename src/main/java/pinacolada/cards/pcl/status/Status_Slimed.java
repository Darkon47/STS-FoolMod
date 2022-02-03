package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.utilities.PCLActions;

public class Status_Slimed extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Status_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, PCLCardTarget.None);

    public Status_Slimed()
    {
        super(DATA, false);

        Initialize(0, 0, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            PCLActions.Bottom.GainEnergyNextTurn(magicNumber);
        }
    }
}