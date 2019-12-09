package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.orbs.Aether;
import eatyourbeets.powers.animator.OrbCore_AetherPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Aether extends OrbCore
{
    public static final String ID = Register(OrbCore_Aether.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Aether()
    {
        super(ID, 1);

        Initialize(0,0, VALUE,1);
        
        SetEvokeOrbCount(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Aether(), true);
        }

        GameActions.Bottom.StackPower(new OrbCore_AetherPower(p, 1));
    }
}