package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class RitesOfPassing extends EternalCard
{
    public static final PCLCardData DATA = Register(RitesOfPassing.class).SetSkill(1, CardRarity.RARE, PCLCardTarget.None);

    public RitesOfPassing()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue + CombatStats.CardsExhaustedThisTurn().size() * secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(secondaryValue);
        PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(GetXValue());
        if (CheckPrimaryCondition(true) && info.IsSynergizing && info.TryActivateLimited())
        {
            PCLActions.Bottom.PlayFromPile(name, 1, m, p.exhaustPile);
        }
    }
}