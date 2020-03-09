package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_Dizziness extends AnimatorCard_Curse
{
    public static final Dazed DAZED = new Dazed();
    public static final EYBCardData DATA = Register(Curse_Dizziness.class).SetCurse(-2, EYBCardTarget.None);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
        DATA.AddPreview(new FakeAbstractCard(DAZED), false);
    }

    public Curse_Dizziness()
    {
        super(DATA);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInDrawPile(DAZED.makeCopy());
        GameActions.Bottom.Flash(this);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }
}