package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Overheat extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Overheat.class).SetStatus(0, CardRarity.COMMON, EYBCardTarget.None);

    public Overheat()
    {
        super(DATA);

        Initialize(0, 0, 3);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not autoplay
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Draw(2);
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
            GameActions.Bottom.Callback(__ ->
            {
                for (AbstractCard c : GameUtilities.GetAllCopies(this))
                {
                    c.baseMagicNumber += 1;
                    c.applyPowers();
                }
            });
        }
    }
}