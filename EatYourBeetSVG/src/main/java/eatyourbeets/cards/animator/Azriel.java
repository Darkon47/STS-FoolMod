package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.AzrielPower;
import eatyourbeets.powers.common.PlayerFlightPower;

public class Azriel extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Azriel.class.getSimpleName());

    public Azriel()
    {
        super(ID, 3, CardType.POWER, CardTarget.SELF);

        Initialize(0,0, 1);

        this.isEthereal = true;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlayerFlightPower(p, 2), 2);
        GameActionsHelper.ApplyPower(p, p, new AzrielPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            this.isEthereal = false;
        }
    }
}