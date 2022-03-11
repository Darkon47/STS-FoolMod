package pinacolada.cards.fool.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.pcl.Air;
import pinacolada.utilities.PCLActions;

public class Rena extends FoolCard
{
    public static final PCLCardData DATA = Register(Rena.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Rena()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 1);

        SetAffinity_Green(1,0,0);
        SetAffinity_Light(1, 0 ,0);

        SetAffinityRequirement(PCLAffinity.Green, 5);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CheckAffinity(PCLAffinity.Green) && CombatStats.TryActivateSemiLimited(cardID) && TrySpendAffinity(PCLAffinity.Green))
        {
            PCLActions.Bottom.GainBlur(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.EvokeOrb(1)
                .SetFilter(o -> Air.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        PCLActions.Bottom.GainBlur(1);
                    }
                    else
                    {
                        PCLActions.Bottom.ChannelOrb(new Air());
                    }
                });
    }
}