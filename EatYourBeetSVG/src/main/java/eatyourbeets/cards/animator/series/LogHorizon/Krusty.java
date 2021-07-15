package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Krusty extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Krusty.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Normal)
            .SetSeriesFromClassPackage();

    public Krusty()
    {
        super(DATA);

        Initialize(28, 0, 3, 3);
        SetUpgrade(1, 0, 1, 1);

        SetAutoplay(true);

        SetAffinity_Red(2, 0, 1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            ((EYBCard) c).AddScaling(AffinityType.Red, secondaryValue);
            c.flash();
        });
    }
}