package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Xiangling_Guoba;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Xiangling extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Xiangling.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage(true)
            .PostInitialize(data -> data.AddPreview(new Xiangling_Guoba(), false));

    public Xiangling()
    {
        super(DATA);

        Initialize(6, 0, 3, 0);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d.AddCallback(m, (enemy, __) -> {
            if (GameUtilities.GetPowerAmount(BurningPower.POWER_ID) > 0 || GameUtilities.GetPowerAmount(FreezingPower.POWER_ID) > 0) {
                GameActions.Bottom.MakeCardInDrawPile(new Xiangling_Guoba());
            }
        }));

        if (info.IsSynergizing) {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}

