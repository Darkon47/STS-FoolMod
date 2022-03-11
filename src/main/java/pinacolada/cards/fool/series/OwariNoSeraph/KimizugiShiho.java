package pinacolada.cards.fool.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KimizugiShiho extends FoolCard
{
    public static final PCLCardData DATA = Register(KimizugiShiho.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(0,2, 1,1);
        SetUpgrade(0,0, 0, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetCostUpgrade(-1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.Cycle(name, secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, false, false)
                .SetFilter(c -> c instanceof PCLCard && ((PCLCard) c).series != null)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .AddCallback(cards ->
                {
                    if (cards.size() > 0 && cards.get(0) instanceof PCLCard)
                    {
                        PCLActions.Top.FetchFromPile(name, 1, player.discardPile)
                                .SetOptions(true, false)
                                .SetFilter(cards.get(0), PCLGameUtilities::IsSameSeries);
                    }

                    if (info.IsSynergizing && CombatStats.TryActivateSemiLimited(cardID)) {
                        PCLActions.Bottom.Cycle(name, secondaryValue);
                    }
                });
    }
}