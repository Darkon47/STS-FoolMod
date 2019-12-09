package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class DolaCouronne extends AnimatorCard
{
    public static final String ID = Register(DolaCouronne.class.getSimpleName(), EYBCardBadge.Exhaust);

    public DolaCouronne()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,9, 10);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainBlock(this.magicNumber);
            GameActions.Bottom.GainArtifact(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}