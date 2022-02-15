package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Centering extends EternalCard
{
    public static final PCLCardData DATA = Register(Centering.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None);

    public Centering()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return GetXValue() > 0 ? super.GetBlockInfo() : null;
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return super.ModifyBlock(enemy, amount + GetXValue());
    }

    @Override
    public int GetXValue() {
        return magicNumber * PCLJUtils.Count(PCLCombatStats.MatchingSystem.ResolveMeter.Next, n -> n.Type != PCLAffinity.General && n.Type != PCLAffinity.Silver);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(GetXValue());
        for (int i = 0; i < PCLCombatStats.MatchingSystem.ResolveMeter.Next.size(); i++) {
            PCLActions.Bottom.RerollAffinity(i, PCLAffinity.Silver);
        }

    }
}