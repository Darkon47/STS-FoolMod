package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_Depression extends PCLCard
{
    public static final PCLCardData DATA = Register(Curse_Depression.class)
            .SetCurse(-2, PCLCardTarget.None, false);

    public Curse_Depression()
    {
        super(DATA);
        Initialize(0,0,1,0);
        SetAffinity_Dark(1);

        SetEthereal(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.DiscardFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .SetFilter(card -> !PCLGameUtilities.IsHindrance(card));

        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Bottom.GainWisdom(magicNumber);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

}