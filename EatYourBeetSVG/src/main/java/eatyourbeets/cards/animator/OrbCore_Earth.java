package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.orbs.Earth;
import eatyourbeets.powers.OrbCore_EarthPower;
import eatyourbeets.powers.OrbCore_LightningPower;

public class OrbCore_Earth extends AnimatorCard
{
    public static final String ID = CreateFullID(OrbCore_Earth.class.getSimpleName());

    public static final int VALUE = 6;

    public OrbCore_Earth()
    {
        super(ID, 2, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Earth(), true);
        GameActionsHelper.ApplyPower(p, p, new OrbCore_EarthPower(p, 1), 1);
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