package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.colorless.QuestionMark;

public class MysteryCard extends AbstractMysteryCard
{
    public static final PCLCardData DATA = Register(MysteryCard.class)
            .SetImagePath(QuestionMark.DATA.ImagePath)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.AoE)
            .SetColor(CardColor.COLORLESS);

    public MysteryCard() {
        this(false);
    }

    public MysteryCard(boolean isDummy)
    {
        super(DATA, isDummy, CardRarity.COMMON);
        Initialize(0, 0, 1, 0);
    }
}