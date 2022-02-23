package pinacolada.cards.fool.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Wiz extends FoolCard
{
    public static final PCLCardData DATA = Register(Wiz.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetTraits(PCLCardTrait.Demon, PCLCardTrait.Undead, PCLCardTrait.Spellcaster)
            .SetSeriesFromClassPackage();

    public Wiz()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
        SetAffinity_Orange(1);

        SetPurge(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (CombatStats.CanActivateLimited(cardID))
        {
            SetPurge(!(PCLCombatStats.MatchingSystem.WouldMatch(this)));
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, true)
        .AddCallback(() ->
        { //
            PCLActions.Top.SelectFromPile(name, 1, player.exhaustPile)
            .SetOptions(false, false)
            .SetFilter(c -> !c.cardID.equals(Wiz.DATA.ID))
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    PCLActions.Bottom.MakeCardInHand(cards.get(0).makeStatEquivalentCopy());
                }
            });
        });

        if (PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber && info.TryActivateLimited())
        {
            PCLActions.Last.ModifyAllInstances(uuid, c -> ((PCLCard)c).SetPurge(true));
        }
    }
}