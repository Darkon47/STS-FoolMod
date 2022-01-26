package pinacolada.cards.pcl.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_StackPower;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ainz extends PCLCard
{
    public static final PCLCardData DATA = Register(Ainz.class)
            .SetPower(8, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();
    public static final PCLAffinity[] AFFINITIES = new PCLAffinity[] {PCLAffinity.Red, PCLAffinity.Blue, PCLAffinity.Orange, PCLAffinity.Dark};
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
        SetCostUpgrade(-1);

        SetProtagonist(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Motivate(this, 1);
        PCLActions.Bottom.GainDesecration(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new AinzPower(p, this, magicNumber));
    }

    public static class AinzPower extends PCLPower
    {
        private final PCLCard source;

        public AinzPower(AbstractPlayer owner, PCLCard source, int amount)
        {
            super(owner, Ainz.DATA);
            this.source = source;
            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE, 0.9f, 1.1f);
            PCLActions.Bottom.BorderLongFlash(Color.valueOf("3d0066"));
            PCLActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.9f, 1.1f);
            PCLCombatStats.MatchingSystem.Powers.get(PCLAffinity.Dark.ID).SetEnabled(true);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();
            PCLActions.Bottom.ObtainAffinityToken(PCLAffinity.Dark, false);

            final int amount = PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Dark);
            choices.Initialize(source, true);
            choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.TemporaryStrength, amount));
            choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.TemporaryDexterity, amount));
            choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.TemporaryFocus, amount));
            choices.Select(1, null);

            this.flash();
        }
    }
}