package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class HastyJudgment extends EternalCard
{
    public static final PCLCardData DATA = Register(HastyJudgment.class).SetSkill(0, CardRarity.COMMON);

    public HastyJudgment()
    {
        super(DATA);

        Initialize(0, 2, 0, 0);
        SetUpgrade(0, 0, 0, 0);

        SetEthereal(true);
    }

    public void OnUpgrade() {
        SetHaste(true);
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        for (int i = 0; i < PCLCombatStats.MatchingSystem.ResolveMeter.Next.size(); i++) {
            PCLActions.Bottom.RerollAffinity(i, PCLAffinity.Light, PCLAffinity.Dark);
        }
    }
}