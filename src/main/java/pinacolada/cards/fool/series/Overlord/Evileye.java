package pinacolada.cards.fool.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Evileye extends FoolCard
{
    public static final PCLCardData DATA = Register(Evileye.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Evileye()
    {
        super(DATA);

        Initialize(0,2, 2, 3);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Blue, 3);
        SetAffinityRequirement(PCLAffinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        if (TrySpendAffinity(PCLAffinity.Blue, PCLAffinity.Dark)) {
            int totalToTransfer = magicNumber;
            for (AbstractPower power : player.powers) {
                if (totalToTransfer <= 0) {
                    break;
                }
                for (PCLPowerHelper commonDebuffHelper : PCLGameUtilities.GetPCLCommonDebuffs()) {
                    if (commonDebuffHelper.ID.equals(power.ID)) {
                        int toTransfer = Math.min(totalToTransfer, power.amount);
                        PCLActions.Top.ReducePower(power, toTransfer);
                        PCLActions.Top.ApplyPower(TargetHelper.RandomEnemy(), commonDebuffHelper, toTransfer);
                        totalToTransfer -= toTransfer;
                        break;
                    }
                }
            }
        }
        PCLActions.Bottom.ApplyBlinded(p, m, magicNumber);
        Dark d = new Dark();
        PCLActions.Bottom.ChannelOrb(d).AddCallback(() -> {
            int debuffCount = PCLGameUtilities.GetDebuffsCount(m);
            PCLActions.Bottom.TriggerOrbPassive(d, debuffCount);
        });
    }
}