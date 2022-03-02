package pinacolada.powers.fool;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.interfaces.subscribers.OnGainPowerBonusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.utilities.PCLActions;

import static pinacolada.cards.fool.special.IonizingStorm.LIGHTNING_BONUS;

public class IonizingStormPower extends PCLPower implements OnOrbPassiveEffectSubscriber, OnGainPowerBonusSubscriber
{
    public static final String POWER_ID = CreateFullID(IonizingStormPower.class);

    public IonizingStormPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Subscribe(this);
        PCLActions.Bottom.AddPowerEffectEnemyBonus(ElectrifiedPower.POWER_ID, PCLCombatStats.GetEffectBonus(ElectrifiedPower.POWER_ID));
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Subscribe(this);
        PCLActions.Bottom.AddPowerEffectEnemyBonus(ElectrifiedPower.POWER_ID, -PCLCombatStats.GetEffectBonus(ElectrifiedPower.POWER_ID) / 2);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, LIGHTNING_BONUS * amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Lightning.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.evokeAmount * LIGHTNING_BONUS * amount / 100);
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Lightning.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount * LIGHTNING_BONUS * amount / 100);
        }
    }

    private void makeMove(AbstractOrb orb, int applyAmount) {
        PCLActions.Bottom.ApplyElectrified(TargetHelper.RandomEnemy(), applyAmount).CanStack(true);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Supercharged, amount);
    }

    @Override
    public int OnGainPowerBonus(String powerID, PCLCombatStats.Type gainType, int amount) {
        if (ElectrifiedPower.POWER_ID.equals(powerID) && gainType == PCLCombatStats.Type.Effect) {
            return amount * 2;
        }
        return amount;
    }
}
