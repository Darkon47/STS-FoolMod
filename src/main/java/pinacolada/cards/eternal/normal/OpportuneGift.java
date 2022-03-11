package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class OpportuneGift extends EternalCard
{
    public static final PCLCardData DATA = Register(OpportuneGift.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None);

    public OpportuneGift()
    {
        super(DATA);

        Initialize(0, 2, 2, 1);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        if (IsStarter()) {
            PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
        }
        if (info.IsSynergizing && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.FetchFromPile(name, 1, player.discardPile)
                    .SetOptions(true, false);
        }
    }
}