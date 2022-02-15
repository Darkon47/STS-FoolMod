package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class DeteriorationOfPrinciples extends EternalCard
{
    public static final PCLCardData DATA = Register(DeteriorationOfPrinciples.class).SetSkill(2, CardRarity.RARE, PCLCardTarget.AoE);

    public DeteriorationOfPrinciples()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 1, 0);

        SetDark();
        SetExhaust(true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLCombatStats.MatchingSystem.ResolveMeter.Resolve();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = GetXValue();
        PCLCombatStats.MatchingSystem.ResolveMeter.TrySpendResolve(PCLCombatStats.MatchingSystem.ResolveMeter.Resolve());
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        PCLCombatStats.AddEffectBonus(VulnerablePower.POWER_ID, amount);
    }
}