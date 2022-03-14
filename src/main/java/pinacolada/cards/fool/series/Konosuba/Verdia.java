package pinacolada.cards.fool.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Verdia extends FoolCard
{
    public static final PCLCardData DATA = Register(Verdia.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetTraits(PCLCardTrait.Demon, PCLCardTrait.Undead)
            .SetSeriesFromClassPackage();

    public Verdia()
    {
        super(DATA);

        Initialize(1, 20, 1);
        SetUpgrade(1, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Dark, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.SelectFromPile(name, 1, player.hand)
                .SetOptions(false, false)
                .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> (upgraded || PCLGameUtilities.HasRedAffinity(c) || PCLGameUtilities.HasDarkAffinity(c)) && (c.baseDamage >= 0 || c.baseBlock >= 0))
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        for (PCLCardAffinity af : affinities.GetCardAffinities(false)) {
                            PCLActions.Bottom.IncreaseScaling(c, af.type, af.scaling);
                        }
                        c.flash();
                    }
                });
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        
        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.IncreaseScaling(this, PCLAffinity.Red, 1);
        }
        if (TrySpendAffinity(PCLAffinity.Dark)) {
            PCLActions.Bottom.IncreaseScaling(this, PCLAffinity.Red, 1);
        }
    }
}