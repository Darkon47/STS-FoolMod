package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomGoblins;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class GoblinKing extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinKing.class).SetStatus(1, CardRarity.RARE, EYBCardTarget.None);

    public GoblinKing()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetSeries(CardSeries.GoblinSlayer);
        SetAffinity(0, 0, 0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Add(new CreateRandomGoblins(3));
        }
    }
}