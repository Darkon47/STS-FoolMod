package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Laby extends PCLCard
{
    public static final PCLCardData DATA = Register(Laby.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainThorns(magicNumber);
        PCLActions.Bottom.StackPower(new LabyPower(p, secondaryValue));
    }

    public static class LabyPower extends PCLPower implements OnTryApplyPowerListener
    {
        public LabyPower(AbstractCreature owner, int amount)
        {
            super(owner, Laby.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            ResetAmount();

            PCLActions.Bottom.ApplyConstricted(TargetHelper.AllCharacters(), amount)
            .ShowEffect(false, true);
            SFX.Play(SFX.POWER_CONSTRICTED);
            this.flashWithoutSound();
        }

        @Override
        public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
        {
            PCLPowerHelper ph = PCLJUtils.Find(PCLGameUtilities.GetPCLCommonDebuffs(), ph2 -> ph2.ID.equals(power.ID));
            int applyAmount = power.amount;
            if (target == owner && ph != null && power.amount > 0)
            {
                if (!PCLGameUtilities.IsPlayerTurn() && (VulnerablePower.POWER_ID.equals(ph.ID) || WeakPower.POWER_ID.equals(ph.ID) || FrailPower.POWER_ID.equals(ph.ID))) {
                    applyAmount += 1;
                }
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), ph, applyAmount);
                flashWithoutSound();
            }

            return true;
        }

    }
}