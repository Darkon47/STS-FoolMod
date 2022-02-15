package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.List;

public class PrimordialEcho extends EternalCard
{
    public static final PCLCardData DATA = Register(PrimordialEcho.class)
            .SetMaxCopies(1)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None);

    public PrimordialEcho()
    {
        super(DATA);

        Initialize(0, 2, 3, 1);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetDark();
        SetExhaust(true);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        List<AbstractCard> cards = CombatStats.CardsPlayedThisCombat(0);
        if (cards.size() > 0) {
            PCLActions.Bottom.MakeCardInHand(cards.get(0).makeStatEquivalentCopy())
                    .AddCallback(c -> {
                        if (CheckSpecialCondition(true) && info.TryActivateLimited()) {
                            PCLGameUtilities.ModifyCostForCombat(c, 0, false);
                        }
                        else {
                            PCLGameUtilities.ModifyCostForTurn(c, 0, false);
                        }

                    });
        }
    }

    public boolean CheckSpecialCondition(boolean tryUse){
        return CheckPrimaryCondition(tryUse) && CombatStats.CanActivateLimited(cardID);
    }
}