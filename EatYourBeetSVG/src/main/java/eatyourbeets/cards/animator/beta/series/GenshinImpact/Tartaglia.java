package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Delusion;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tartaglia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tartaglia.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Delusion(), false));

    public Tartaglia() {
        super(DATA);

        Initialize(12, 0);
        SetUpgrade(4, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            amount += GameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) * 2.0;
        }

        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT)
                .AddCallback(m.currentBlock, (initialBlock, target) ->
                {
                    if (GameUtilities.IsDeadOrEscaped(target) && CombatStats.TryActivateLimited(cardID))
                    {
                        GameActions.Bottom.MakeCardInDrawPile(new Curse_Delusion());
                        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
                    }

                });

        GameActions.Bottom.RemovePower(p, m, BurningPower.POWER_ID);
    }
}