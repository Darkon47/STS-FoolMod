package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.utilities.GameUtilities;

public class Tatsumaki extends AnimatorCard
{
    public static final String ID = Register(Tatsumaki.class);

    public Tatsumaki()
    {
        super(ID, 2, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.ChannelOrb(new Aether(), true);

        if (GameUtilities.GetUniqueOrbsCount() >= 3 && EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainEnergy(2);
            GameActions.Bottom.Draw(2);
        }

        if (upgraded)
        {
            IntellectPower.PreserveOnce();
        }
    }
}