package pinacolada.cards.fool.series.RozenMaiden;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;


public class TomoeKashiwaba extends FoolCard
{
    public static final PCLCardData DATA = Register(TomoeKashiwaba.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    public TomoeKashiwaba()
    {
        super(DATA);

        Initialize(0, 5, 2, 4);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        PCLGameUtilities.Retain(card);
                        if (card instanceof PCLCard) {
                            PCLActions.Bottom.AddAffinities(((PCLCard) card).affinities);
                        }
                    }
                });

        if (CheckAffinity(PCLAffinity.General) && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> PCLActions.Bottom.TryChooseGainAffinity(name, secondaryValue, PCLAffinity.Basic()));
        }
    }
}
