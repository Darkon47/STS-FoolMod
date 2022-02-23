package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolPower;
import pinacolada.powers.common.BlindedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.Map;

public class KanameTousen extends FoolCard {
    public static final PCLCardData DATA = Register(KanameTousen.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public KanameTousen() {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 4, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Blue, 8);
        SetAffinityRequirement(PCLAffinity.Dark, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);

        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            if (mo.hasPower(BlindedPower.POWER_ID)) {
                PCLActions.Bottom.GainTemporaryArtifact(secondaryValue);
                break;
            }
        }
        PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), magicNumber);

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Blue, PCLAffinity.Dark)
                .CancellableFromPlayer(true)
                .AddConditionalCallback(() -> PCLActions.Bottom.StackPower(player, new KanameTousenPower(player, 1)));
    }


    public static class KanameTousenPower extends FoolPower implements OnTryApplyPowerListener
    {
        private final HashMap<String, Integer> counts = new HashMap<>();

        public KanameTousenPower(AbstractPlayer owner, int amount)
        {
            super(owner, KanameTousen.DATA);

            this.amount = amount;
        }

        @Override
        public void atEndOfRound() {
            ReducePower(1);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            if (owner.powers != null) {
                for (AbstractPower power : owner.powers) {
                    if (power.amount < 0) {
                        counts.merge(power.ID, power.amount * 2, Integer::sum);
                        power.amount = power.amount * -1;
                    }
                }
            }
            updateDescription();

        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                AbstractPower power = PCLGameUtilities.GetPower(owner, entry.getKey());
                if (power != null) {
                    power.amount += entry.getValue();
                }
            }
        }

        @Override
        public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction abstractGameAction) {
            if (target == owner && power.amount < 0) {
                counts.merge(power.ID, power.amount * 2, Integer::sum);
                power.amount = power.amount * -1;
            }
            return true;
        }
    }
}