package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Aether;
import eatyourbeets.utilities.GameUtilities;

public class Tatsumaki extends AnimatorCard
{
    public static final String ID = Register(Tatsumaki.class.getSimpleName(), EYBCardBadge.Special);

    public Tatsumaki()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Aether(), true);
        GameActions.Bottom.GainIntellect(magicNumber);

        if (GameUtilities.GetUniqueOrbsCount() >= 3 && EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainEnergy(2);
            GameActions.Bottom.Draw(2);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}