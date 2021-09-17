package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.ListSelection;

import java.util.Comparator;

public class Enchantment3 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment3.class);
    public static final int INDEX = 3;
    public static final int UP5_BLOCK = 3;

    public Enchantment3()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 3 || auxiliaryData.form == 4)
        {
            upgradeSecondaryValue(1);
        }
        else if (auxiliaryData.form == 5)
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 5;
    }

    @Override
    protected String GetRawDescription()
    {
        if (auxiliaryData.form == 5) {
            return super.GetRawDescription(UP5_BLOCK);
        }
        return super.GetRawDescription();
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return super.CanUsePower(cost) && (auxiliaryData.form != 5 || player.currentBlock >= UP5_BLOCK);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        super.PayPowerCost(cost);

        if (auxiliaryData.form == 5)
        {
            GameActions.Bottom.LoseBlock(UP5_BLOCK);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        switch (auxiliaryData.form) {
            case 0:
                GameActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(ListSelection.First(0), 1);
                return;
            case 1:
            case 2:
                GameActions.Bottom.ReduceCommonDebuffs(player, magicNumber).SetSelection(auxiliaryData.form == 1 ? ListSelection.First(0) : ListSelection.Last(0), 1).AddCallback(po -> {
                    if (po.size() > 0) {
                        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                            GameActions.Bottom.ApplyPower(player,mo,po.get(0));
                        }
                    }
                });
                return;
            case 3:
            case 4:
                GameActions.Bottom.RemoveCommonDebuffs(player, auxiliaryData.form == 3 ? ListSelection.First(0) : ListSelection.Last(0), 1);
                return;
            case 5:
                GameActions.Bottom.ReduceDebuffs(player, magicNumber)
                        .SetSelection(ListSelection.First(0), 1)
                        .Sort(Comparator.comparingInt(a -> -a.amount));
        }
    }
}