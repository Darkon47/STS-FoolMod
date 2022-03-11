package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class PledgeOfPropriety extends EternalCard
{
    public static final PCLCardData DATA = Register(PledgeOfPropriety.class).SetSkill(3, CardRarity.UNCOMMON, PCLCardTarget.None);

    public PledgeOfPropriety()
    {
        super(DATA);

        Initialize(0, 9, 2, 5);
        SetUpgrade(0, 0, 1, 1);

        SetLight();
    }


    @Override
    public int GetXValue() {
        return magicNumber * PCLCombatStats.MatchingSystem.ResolveMeter.Resolve();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, GetXValue()));
        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .AddCallback(() ->
                {
                    PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, secondaryValue));
                });
    }
}