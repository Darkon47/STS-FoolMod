package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Defend_GATE extends Defend
{
    public static final String ID = Register(Defend_GATE.class).ID;

    public Defend_GATE()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6);
        SetUpgrade(0, 3);

        SetRetain(true);
        SetSeries(CardSeries.GATE);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}