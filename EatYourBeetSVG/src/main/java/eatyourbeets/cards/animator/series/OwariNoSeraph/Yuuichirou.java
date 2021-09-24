package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yuuichirou_Asuramaru;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Yuuichirou extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(Yuuichirou.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data
                    .AddPreview(new Yuuichirou_Asuramaru(), true));

    public Yuuichirou()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1, 1, 1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);

        if ((ForceStance.IsActive() || AgilityStance.IsActive())) {
            CombatStats.Affinities.Force.SetEnabled(true);
            CombatStats.Affinities.Agility.SetEnabled(true);
            CombatStats.onEndOfTurn.Subscribe(this);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        CombatStats.onEndOfTurn.Unsubscribe(this);
        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            CombatStats.Affinities.Agility.SetEnabled(false);
        }
        if (GameUtilities.InStance(AgilityStance.STANCE_ID)) {
            CombatStats.Affinities.Force.SetEnabled(false);
        }
    }
}