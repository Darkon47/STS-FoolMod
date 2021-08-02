package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public RoyMustang()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 1);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Light(1);

        SetEvokeOrbCount(1);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (CombatStats.CardsExhaustedThisTurn() * magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.ChannelOrbs(Fire::new, Math.min(p.orbs.size(), GameUtilities.GetEnemies(true).size()));
    }
}