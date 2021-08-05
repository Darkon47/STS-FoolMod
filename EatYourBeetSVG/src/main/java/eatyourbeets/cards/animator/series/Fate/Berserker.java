package eatyourbeets.cards.animator.series.Fate;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Berserker extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Berserker.class)
            .SetAttack(3, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Berserker()
    {
        super(DATA);

        Initialize(18, 0, 2, 12);
        SetUpgrade(6, 0, 0, 0);

        SetAffinity_Red(2, 0, 6);

        SetAffinityRequirement(AffinityType.Red, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (m != null)
        {
            GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY)
            .AddCallback(m.currentBlock, (initialBlock, target) ->
            {
                if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
                {
                    GameActions.Bottom.GainBlock(this.secondaryValue);
                }
            });
            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }

        if (CheckAffinity(AffinityType.Red))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }
}