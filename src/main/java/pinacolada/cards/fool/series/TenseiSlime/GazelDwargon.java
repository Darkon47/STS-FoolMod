package pinacolada.cards.fool.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GazelDwargon extends FoolCard
{
    public static final PCLCardData DATA = Register(GazelDwargon.class)
            .SetPower(-1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public GazelDwargon()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Red(1);
        SetAffinity_Orange(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {
            PCLActions.Bottom.GainPlatedArmor(stacks + secondaryValue);
        }
        PCLActions.Bottom.StackPower(new GazelDwargonPower(p, stacks * magicNumber));
    }

    public static class GazelDwargonPower extends FoolPower
    {
        public GazelDwargonPower(AbstractPlayer owner, int amount)
        {
            super(owner, GazelDwargon.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.MatchingSystem.GetPower(PCLAffinity.Orange).SetEnabled(true);
        }


        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);

            PCLActions.Last.Callback(() -> {
                PCLCombatStats.MatchingSystem.GetPower(PCLAffinity.Orange).SetEnabled(true);
            });
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (isPlayer)
            {
                CombatStats.BlockRetained += amount;
            }
        }
    }
}