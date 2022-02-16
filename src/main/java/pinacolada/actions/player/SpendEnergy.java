package pinacolada.actions.player;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.EYBActionWithCallbackT2;
import pinacolada.powers.PCLCombatStats;

public class SpendEnergy extends EYBActionWithCallbackT2<Integer, Integer>
{
    protected boolean canSpendLess;

    public SpendEnergy(int amount, boolean canSpendLess)
    {
        super(ActionType.ENERGY);

        this.canSpendLess = canSpendLess;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        int energy = EnergyPanel.getCurrentEnergy();
        if (energy >= amount || canSpendLess)
        {
            energy = Math.min(energy, amount);
            if (CheckConditions(energy))
            {
                int finalEnergy = PCLCombatStats.OnTrySpendEnergy(null, AbstractDungeon.player, energy);
                player.loseEnergy(finalEnergy);
                Complete(finalEnergy);
                return;
            }
        }

        Complete();
    }
}
