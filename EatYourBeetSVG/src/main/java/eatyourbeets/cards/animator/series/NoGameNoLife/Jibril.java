package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.actions.orbs.ShuffleOrbs;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Jibril extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Jibril.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Jibril()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.VIOLET.cpy(), ShockWaveEffect.ShockWaveType.ADDITIVE), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ChannelOrb(new Dark());

        if (isSynergizing)
        {
            GameActions.Bottom.Add(new ShuffleOrbs(1));
            GameActions.Bottom.Add(new TriggerOrbPassiveAbility(magicNumber, false, true));
        }

        CombatStats.Affinities.Intellect.Retain();
    }
}
