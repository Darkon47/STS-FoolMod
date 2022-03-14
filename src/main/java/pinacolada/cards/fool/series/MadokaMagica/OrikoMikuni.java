package pinacolada.cards.fool.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_Scry;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class OrikoMikuni extends FoolCard
{
    public static final PCLCardData DATA = Register(OrikoMikuni.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        choices.Initialize(this, true);
        choices.AddEffect(new BaseEffect_Scry(magicNumber));
        choices.AddEffect(BaseEffect.Gain(1, PCLPowerHelper.NextTurnDraw));
        choices.AddEffect(BaseEffect.Gain(secondaryValue, PCLPowerHelper.NextTurnBlock));

        if (TrySpendAffinity(PCLAffinity.Blue))
        {
            choices.Select(2, m);
        }
        else
        {
            choices.Select(1, m);
        }
    }
}