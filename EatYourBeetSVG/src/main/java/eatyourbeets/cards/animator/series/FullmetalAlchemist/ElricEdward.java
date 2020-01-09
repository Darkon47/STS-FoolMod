package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class ElricEdward extends AnimatorCard
{
    public static final String ID = Register(ElricEdward.class);

    public ElricEdward()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4, 0, 1);
        SetUpgrade(4, 0, 0);

        AddExtendedDescription();

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.Cycle(name, 1).AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                switch (cards.get(0).type)
                {
                    case ATTACK:
                        GameActions.Bottom.ChannelOrb(new Lightning(), true);
                        break;

                    case SKILL:
                        GameActions.Bottom.ChannelOrb(new Frost(), true);
                        break;

                    case POWER:
                        GameActions.Bottom.ChannelOrb(new Earth(), true);
                        break;
                }
            }
        });
    }
}