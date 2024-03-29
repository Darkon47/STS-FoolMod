package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YasutoraSado extends FoolCard
{
    public static final PCLCardData DATA = Register(YasutoraSado.class).SetAttack(0, CardRarity.COMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YasutoraSado()
    {
        super(DATA);

        Initialize(2, 0, 5);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetCooldown(2, 0, this::OnCooldownCompleted);
    }


    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy == null || enemy.intent == null) {
            return super.ModifyDamage(enemy, amount);
        }
        return super.ModifyDamage(enemy, (enemy.currentBlock > 0 || PCLGameUtilities.IsDebuffing(enemy.intent)) ? (amount + magicNumber) : amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(BaseEffect.EnterStance(PCLStanceHelper.MightStance));
            choices.AddEffect(BaseEffect.EnterStance(PCLStanceHelper.VelocityStance));
        }

        choices.Select(PCLActions.Top, 1, m)
                .CancellableFromPlayer(true);

        PCLActions.Last.Exhaust(this);
    }
}