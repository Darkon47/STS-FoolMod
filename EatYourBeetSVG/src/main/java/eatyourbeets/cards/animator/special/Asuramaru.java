package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Yuuichirou;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Yuuichirou.DATA.Series);

    public Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new DemonFormPower(p, secondaryValue));
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainWillpower(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
        GameActions.Bottom.MakeCardInHand(new Wound());
    }
}