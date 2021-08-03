package eatyourbeets.cards.animator.ultrarare;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class Giselle extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Giselle.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GATE);

    public Giselle()
    {
        super(DATA);

        Initialize(24, 0, 4);
        SetUpgrade(8, 0, 0);

        SetAffinity_Red(2, 0, 3);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
        GameActions.Bottom.VFX(new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.5f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }
}