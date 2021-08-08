package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.Kuroyukihime;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class BlackLotus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BlackLotus.class)
            .SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Kuroyukihime.DATA.Series);

    public BlackLotus()
    {
        super(DATA);

        Initialize(7, 5, 2, 2);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Green(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX(SFX.ATTACK_DEFECT_BEAM);
        GameActions.Bottom.VFX(VFX.SweepingBeam(p.hb, VFX.FlipHorizontally(), new Color(0.24f, 0, 0.4f, 1f)), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);

        GameActions.Bottom.RetainPower(Affinity.Red);
        GameActions.Bottom.RetainPower(Affinity.Green);

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
        }

        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.GainBlur(secondaryValue);
        }
    }
}