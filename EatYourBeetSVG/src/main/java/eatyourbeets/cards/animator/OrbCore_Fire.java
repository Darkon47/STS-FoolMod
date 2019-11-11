package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.OrbCore_FirePower;

public class OrbCore_Fire extends AnimatorCard
{
    public static final String ID = Register(OrbCore_Fire.class.getSimpleName(), EYBCardBadge.Special);

    public static final int VALUE = 3;

    public OrbCore_Fire()
    {
        super(ID, 0, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActionsHelper.ChannelOrb(new Fire(), true);
        }

        GameActionsHelper.ApplyPower(p, p, new OrbCore_FirePower(p, 1), 1);
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