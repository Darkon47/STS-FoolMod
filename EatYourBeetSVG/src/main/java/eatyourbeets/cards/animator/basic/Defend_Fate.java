package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_Fate extends Defend
{
    public static final String ID = Register(Defend_Fate.class).ID;

    public Defend_Fate()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.Fate);
        SetAffinity_Blue(1);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + GameUtilities.GetEnemies(true).size() * magicNumber;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}