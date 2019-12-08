package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class Defend_HitsugiNoChaika extends Defend
{
    public static final String ID = Register(Defend_HitsugiNoChaika.class.getSimpleName());

    public Defend_HitsugiNoChaika()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.GainBlock(this.block);
        GameActionsHelper2.Cycle(1, name);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}