package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.PridePower;

public class Pride extends AnimatorCard
{
    public static final String ID = Register(Pride.class.getSimpleName(), EYBCardBadge.Special);

    public Pride()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 1, 3);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }

        GameActions.Bottom.ApplyPower(p, m, new ConstrictedPower(m, p, this.secondaryValue), this.secondaryValue);

        if (!p.hasPower(PridePower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(p, p, new PridePower(p));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            SetEvokeOrbCount(magicNumber);
        }
    }
}