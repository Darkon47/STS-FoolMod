package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Benimaru extends AnimatorCard
{
    public static final String ID = Register(Benimaru.class.getSimpleName(), EYBCardBadge.Special);

    public Benimaru()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(3, 0, 2);

        SetEvokeOrbCount(1);
        SetMultiDamage(true);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ChannelOrb(new Fire(), true);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            if (m1.hasPower(BurningPower.POWER_ID))
            {
                GameActions.Bottom.GainTemporaryHP(magicNumber);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}