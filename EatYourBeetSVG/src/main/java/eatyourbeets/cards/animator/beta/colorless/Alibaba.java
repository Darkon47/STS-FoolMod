package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Alibaba extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Alibaba.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Normal).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Magi);

    public Alibaba()
    {
        super(DATA);

        Initialize(3, 0, 2 , 2);
        SetUpgrade(0, 0, 0 , 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(secondaryValue);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Red, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL).AddCallback(e -> {
                if (e.lastDamageTaken > 0) {
                    if (IsStarter()) {
                        GameActions.Bottom.TriggerOrbPassive(1).SetFilter(o -> Earth.ORB_ID.equals(o.ID));
                    }
                    GameActions.Bottom.ApplyBurning(TargetHelper.Normal(e), magicNumber);
                }
            });
        }
    }

}