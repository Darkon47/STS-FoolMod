package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment4 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment4.class);
    public static final int INDEX = 4;
    public Affinity currentAffinity;

    public Enchantment4()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        if (GetAffinity() == null) {
            return CombatStats.Affinities.GetAffinityLevel(Affinity.General, true) >= cost;
        }
        return CombatStats.Affinities.GetAffinityLevel(GetAffinity(), true) >= cost;
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (GetAffinity() == null) {
            GameActions.Bottom.TryChooseSpendAffinity(name, cost);
        }
        else {
            GameUtilities.TrySpendAffinity(GetAffinity(), affinities.GetRequirement(GetAffinity()), true);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 6;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            GameActions.Bottom.GainRandomAffinityPower(magicNumber, true);
            return;
        }

        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            final AbstractAffinityPower p = CombatStats.Affinities.GetPower(currentAffinity);
            if (p != null) {
                GameActions.Bottom.StackAffinityPower(currentAffinity, magicNumber);
            }
            else {
                GameActions.Bottom.GainEnergyNextTurn(magicNumber);
            }
        }
    }

    public Affinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return Affinity.Red;
            case 2: return Affinity.Green;
            case 3: return Affinity.Blue;
            case 4: return Affinity.Orange;
            case 5: return Affinity.Light;
            case 6: return Affinity.Dark;
            default: return null;
        }
    }
}