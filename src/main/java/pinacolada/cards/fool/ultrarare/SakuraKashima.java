package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class SakuraKashima extends FoolCard_UltraRare {
    public static final PCLCardData DATA = Register(SakuraKashima.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.Rewrite);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public SakuraKashima() {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle) {
            PCLGameEffects.List.ShowCopy(this);
            if (choices.TryInitialize(this))
            {
                for (PCLStanceHelper stance : PCLStanceHelper.ALL.values()) {
                    choices.AddEffect(BaseEffect.EnterStance(stance));
                }
            }

            choices.Select(1, null);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.StackPower(new SakuraKashimaPower(p, magicNumber));
    }

    public static class SakuraKashimaPower extends FoolPower
    {
        public SakuraKashimaPower(AbstractCreature owner, int amount)
        {
            super(owner, SakuraKashima.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            EnablePowers();
        }


        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);

            PCLStance eOldStance = PCLJUtils.SafeCast(newStance, PCLStance.class);
            PCLStance eNewStance = PCLJUtils.SafeCast(newStance, PCLStance.class);

            if (eOldStance != null) {
                final AbstractPCLAffinityPower p = PCLCombatStats.MatchingSystem.GetPower(eOldStance.affinity);
                if (p != null) {
                    PCLActions.Bottom.StackAffinityPower(eOldStance.affinity, amount);
                    p.SetScalingMultiplier(p.scalingMultiplier - 2);
                }
            }
            if (eNewStance != null) {
                final AbstractPCLAffinityPower p = PCLCombatStats.MatchingSystem.GetPower(eNewStance.affinity);
                if (p != null) {
                    p.SetScalingMultiplier(p.scalingMultiplier + 2);
                }
            }

            EnablePowers();
        }

        private void EnablePowers() {
            for (PCLAffinity affinity : PCLAffinity.Extended()) {
                PCLCombatStats.MatchingSystem.GetPower(affinity).SetEnabled(true);
            }
        }
    }
}