package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;

public class Elsword extends AnimatorCard
{
    public static final String ID = Register(Elsword.class);

    public Elsword()
    {
        super(ID, 2, CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(14, 0, 1, 4);
        SetUpgrade(2,  0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ChannelOrb(new Fire(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.ApplyBurning(p, m, secondaryValue);
        GameActions.Bottom.Cycle(name, magicNumber);
    }
}