package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Tatsumaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tatsumaki.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);

        SetEvokeOrbCount(1);
        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Air());
        GameActions.Bottom.GainFocus(magicNumber, true);
        if (IsStarter() && TrySpendAffinity(Affinity.Red, Affinity.Blue)) {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }
    }
}