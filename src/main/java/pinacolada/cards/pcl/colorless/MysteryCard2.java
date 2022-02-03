package pinacolada.cards.pcl.colorless;

import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;

public class MysteryCard2 extends AbstractMysteryCard
{
    public static final PCLCardData DATA = Register(MysteryCard2.class)
            .SetImagePath(QuestionMark.DATA.ImagePath)
            .SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.AoE)
            .SetColor(CardColor.COLORLESS);

    public MysteryCard2() {
        this(false);
    }

    public MysteryCard2(boolean isDummy)
    {
        super(DATA, isDummy, CardRarity.COMMON);
        Initialize(0, 0, 2, 0);
    }

}