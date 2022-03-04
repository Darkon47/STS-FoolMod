package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;

public class IsshinKurosaki extends FoolCard
{
    public static final PCLCardData DATA = Register(IsshinKurosaki.class).SetAttack(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(4, 7, 2, 4);
        SetUpgrade(0, 3, 0);
        SetAffinity_Red(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        makeChoice(m);

        if (TrySpendAffinity(PCLAffinity.Red) || MightStance.IsActive())
        {
            makeChoice(m);
        }
    }

    private void makeChoice(AbstractMonster m) {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(BaseEffect.ApplyToEnemies(magicNumber, PCLPowerHelper.Burning));
            choices.AddEffect(BaseEffect.Gain(secondaryValue, PCLPowerHelper.CounterAttack));
        }
        choices.Select(1, m);
    }
}