package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.OrbCore_PlasmaPower;

public class OrbCore_Plasma extends AnimatorCard
{
    public static final String ID = CreateFullID(OrbCore_Plasma.class.getSimpleName());

    public static final int VALUE = 1;

    public OrbCore_Plasma()
    {
        super(ID, 2, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Plasma(), true);
        GameActionsHelper.ApplyPower(p, p, new OrbCore_PlasmaPower(p, 1), 1);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}