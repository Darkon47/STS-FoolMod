package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class PerfectParry extends EternalCard
{
    public static final PCLCardData DATA = Register(PerfectParry.class).SetSkill(2, CardRarity.COMMON, PCLCardTarget.Self);

    public PerfectParry()
    {
        super(DATA);

        Initialize(0, 3, 4, 13);
        SetUpgrade(0, 1, 0, 3);

        SetLight();
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (PCLCombatStats.MatchingSystem.WouldMatch(this)) {
            amount += secondaryValue;
        }
        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        if (info.IsSynergizing)
        {
            PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
            PCLActions.Bottom.GainCounterAttack(magicNumber);
        }
    }
}