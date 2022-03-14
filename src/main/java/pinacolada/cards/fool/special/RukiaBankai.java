package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class RukiaBankai extends FoolCard
{
    public static final PCLCardData DATA = Register(RukiaBankai.class).SetSkill(-1, CardRarity.SPECIAL, PCLCardTarget.None).SetSeries(CardSeries.Bleach);

    public RukiaBankai()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetExhaust(true);
        SetMultiDamage(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);

        int frostExhaustCount = 0;
        ArrayList<AbstractOrb> frostsToExhaust = new ArrayList<>();

        for (AbstractOrb orb : player.orbs)
        {
            if (Frost.ORB_ID.equals(orb.ID))
            {
                frostsToExhaust.add(orb);
                frostExhaustCount++;

                if (frostExhaustCount >= stacks)
                {
                    break;
                }
            }
        }

        for (AbstractOrb orb : frostsToExhaust)
        {
            PCLActions.Bottom.EvokeOrb(magicNumber, orb);
            PCLActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(), secondaryValue);
        }
    }
}