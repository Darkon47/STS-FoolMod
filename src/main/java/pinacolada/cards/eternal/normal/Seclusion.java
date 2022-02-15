package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Seclusion extends EternalCard
{
    public static final PCLCardData DATA = Register(Seclusion.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.Self);

    public Seclusion()
    {
        super(DATA);

        Initialize(0, 15, 1, 1);
        SetUpgrade(0, 4, 0, 0);

        SetDark();
        SetEthereal(true);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return super.ModifyBlock(enemy, amount - GetXValue());
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetExhaust(CheckPrimaryCondition(false));
    }

    @Override
    public int GetXValue() {
        return magicNumber * PCLCombatStats.MatchingSystem.ResolveMeter.Resolve();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (block > 0) {
            PCLActions.Bottom.GainBlock(block);
        }
        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.GainBlur(secondaryValue);
        }
    }
}