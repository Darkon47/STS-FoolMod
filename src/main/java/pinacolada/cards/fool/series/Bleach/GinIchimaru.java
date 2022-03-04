package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class GinIchimaru extends FoolCard
{
    public static final PCLCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing, PCLCardTarget.Random).SetSeriesFromClassPackage();

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

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Green, 4);
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
        CardEffectChoice choice = SetupChoices(true);
        if (CheckAffinity(PCLAffinity.Red)) {
            choice.AddEffect(BaseCondition.PayAffinity(this, PCLAffinity.Red)
                    .SetChildEffect(BaseEffect.GainAffinityPower(magicNumber, PCLAffinity.Red)));
        }
        if (CheckAffinity(PCLAffinity.Green)) {
            choice.AddEffect(BaseCondition.PayAffinity(this, PCLAffinity.Green)
                    .SetChildEffect(BaseEffect.GainAffinityPower(magicNumber, PCLAffinity.Green)));
        }
        choice.Select(1, m);
    }
}