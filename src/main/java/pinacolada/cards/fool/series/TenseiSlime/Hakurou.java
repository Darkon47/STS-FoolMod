package pinacolada.cards.fool.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;

public class Hakurou extends FoolCard
{
    public static final PCLCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Piercing, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Hakurou()
    {
        super(DATA);

        Initialize(2, 1, 4, 3);
        SetUpgrade(1, 0, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Orange, 4);

        SetHitCount(3,0);
        SetRightHitCount(3);
    }

    @Override
    protected void OnUpgrade()
    {
        AddScaling(PCLAffinity.Red, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.GainBlock(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Red, PCLAffinity.Orange)
                .AddConditionalCallback(() -> {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(BaseEffect.EnterStance(PCLStanceHelper.VelocityStance));
                choices.AddEffect(BaseEffect.GainAffinity(magicNumber, PCLAffinity.Green));
            }
            choices.Select(1, m);
        });
    }
}