package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.OrbCore_LightningPower;

public class OrbCore_Lightning extends AnimatorCard
{
    public static final String ID = CreateFullID(OrbCore_Lightning.class.getSimpleName());

    public static final int VALUE = 11;

    public OrbCore_Lightning()
    {
        super(ID, 1, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, VALUE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(new Lightning(), true);
        GameActionsHelper.ApplyPower(p, p, new OrbCore_LightningPower(p, 1), 1);
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