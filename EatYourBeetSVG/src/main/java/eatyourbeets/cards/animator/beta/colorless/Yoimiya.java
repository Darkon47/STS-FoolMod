package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.BlazingHeat;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Yoimiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoimiya.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.Random).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public Yoimiya()
    {
        super(DATA);

        Initialize(2, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Red, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.DAGGER).AddCallback(e -> {
                if (IsStarter()) {
                    if (e.lastDamageTaken > 0) {
                        GameActions.Bottom.CreateThrowingKnives(magicNumber).SetUpgrade(upgraded);
                    }
                }
            });
        }

        GameActions.Bottom.Callback(() -> {
            GameActions.Bottom.Cycle(name,secondaryValue);
        });

        if (GameUtilities.GetPowerAmount(player, BurningPower.POWER_ID) > 0 && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
    }
}