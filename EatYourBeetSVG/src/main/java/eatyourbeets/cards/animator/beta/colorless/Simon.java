package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Simon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Simon.class).SetAttack(1, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GurrenLagann);

    public Simon()
    {
        super(DATA);

        Initialize(5, 0, 6 , 0);
        SetUpgrade(1, 0, 0 , 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Orange(1);

        SetExhaust(true);
        SetUnique(true,true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 5 == 0)
        {
            this.AddScaling(Affinity.Red, 1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH).AddCallback(e -> {
            GameActions.Bottom.StackPower(new VigorPower(player, e.lastDamageTaken / 2));
        });

        if (CombatStats.Affinities.GetPowerAmount(Affinity.Red) >= magicNumber && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
        }
    }
}