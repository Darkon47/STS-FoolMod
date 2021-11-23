package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Shuna extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shuna.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Shuna()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 1, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainTemporaryHP(secondaryValue);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainSupercharge(magicNumber);
        GameActions.Bottom.GainEndurance(magicNumber);
        GameActions.Bottom.Draw(magicNumber);
    }
}