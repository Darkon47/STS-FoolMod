package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActions;

public class AcuraAkari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraAkari.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }

    public AcuraAkari()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.Chaika);
        SetAffinity(1, 2, 0, 0, 0);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.CreateThrowingKnives(magicNumber));

        if (isSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}