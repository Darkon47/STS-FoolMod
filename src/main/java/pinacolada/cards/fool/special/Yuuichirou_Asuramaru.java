package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yuuichirou_Asuramaru extends FoolCard
{
    public static final PCLCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, PCLCardTarget.Normal)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 1, 3, 4);
        SetUpgrade(0,0,1,0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Dark(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Dark, 5);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.ApplyBlinded(TargetHelper.Normal(m), magicNumber);

        if (TrySpendAffinity(PCLAffinity.Dark)) {
            PCLActions.Bottom.GainMight(secondaryValue);
            PCLActions.Bottom.GainVelocity(secondaryValue);
            PCLActions.Bottom.GainWisdom(secondaryValue);
            PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Red, 1);
            PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Green, 1);
            PCLGameUtilities.AddAffinityPowerUse(PCLAffinity.Blue, 1);
            PCLActions.Bottom.MakeCardInHand(new Status_Burn()).Repeat(2);
        }
    }
}