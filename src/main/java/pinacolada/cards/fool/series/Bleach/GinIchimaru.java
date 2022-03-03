package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GinIchimaru extends FoolCard
{
    public static final PCLCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing, PCLCardTarget.Random).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final int MAX_AMOUNT = 10;

    public GinIchimaru()
    {
        super(DATA);

        Initialize(4, 0, 2, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(0,0,1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
        SetHitCount(2, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() -> makeChoice(m, 1));
    }

    private void makeChoice(AbstractMonster m, int selections) {
        if (choices.TryInitialize(this))
        {
            if (CheckAffinity(PCLAffinity.Red)) {
                choices.AddEffect(new GenericCardEffect_Gin(PCLAffinity.Red, affinities.GetRequirement(PCLAffinity.Red)));
            }
            if (CheckAffinity(PCLAffinity.Green)) {
                choices.AddEffect(new GenericCardEffect_Gin(PCLAffinity.Green,  affinities.GetRequirement(PCLAffinity.Green)));
            }
        }
        choices.Select(selections, m);
    }

    protected static class GenericCardEffect_Gin extends GenericCardEffect
    {
        protected final PCLAffinity affinity;

        public GenericCardEffect_Gin(PCLAffinity affinity, int amount)
        {
            this.affinity = affinity;
            this.amount = amount;
        }

        @Override
        public String GetText()
        {
            return PGR.PCL.Strings.Actions.PayCost(amount, affinity.GetTooltip(), true) + " NL " + PGR.PCL.Strings.Actions.GainAmount(1, affinity.equals(PCLAffinity.Green) ? PGR.Tooltips.Velocity : PGR.Tooltips.Might, true);
        }

        @Override
        public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
        {
            if (PCLGameUtilities.TrySpendAffinity(affinity,amount,true)) {
                PCLActions.Bottom.StackAffinityPower(affinity,1,false);
            }
        }
    }
}