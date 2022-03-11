package pinacolada.cards.fool.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Gabiru extends FoolCard
{
    public static final PCLCardData DATA = Register(Gabiru.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Reptile);

    public Gabiru()
    {
        super(DATA);

        Initialize(0, 4, 2, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        if (IsStarter()) {
            PCLActions.Bottom.Motivate(player.drawPile);
        }
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> PCLGameUtilities.CanRetain(c) && (CheckPrimaryCondition(true) ? PCLGameUtilities.HasGreenAffinity(c) : PCLGameUtilities.HasOrangeAffinity(c))).AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLGameUtilities.Retain(c);
            }
        });
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Orange, true);
    }
}