package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.Kuroyukihime;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.vfx.ColoredSweepingBeamEffect;
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

        Initialize(7, 5, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_DEFECT_BEAM");
        GameActions.Bottom.VFX(new ColoredSweepingBeamEffect(p.hb.cX, p.hb.cY, p.flipHorizontal, Color.valueOf("3d0066")), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.GainBlur(magicNumber);
    }
}