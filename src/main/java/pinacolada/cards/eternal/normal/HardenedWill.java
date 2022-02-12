package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class HardenedWill extends EternalCard
{
    public static final PCLCardData DATA = Register(HardenedWill.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Self);

    public HardenedWill()
    {
        super(DATA);

        Initialize(0, 4, 4, 2);
        SetUpgrade(0, 3, 0, 0);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (PCLCombatStats.MatchingSystem.ResolveMeter.TrySpendResolve(magicNumber)) {
            PCLActions.Bottom.GainMetallicize(magicNumber);
            if (CheckPrimaryCondition(true)) {
                PCLActions.Bottom.GainMetallicize(secondaryValue);
            }
        }
    }
}