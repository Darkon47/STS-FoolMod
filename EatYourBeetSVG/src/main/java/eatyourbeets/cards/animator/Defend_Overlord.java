package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;

public class Defend_Overlord extends Defend
{
    public static final String ID = Register(Defend_Overlord.class.getSimpleName());

    public Defend_Overlord()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.GainBlock(this.block);
        GameActionsHelper2.ModifyAllCombatInstances(uuid, c -> c.baseBlock += c.magicNumber);
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