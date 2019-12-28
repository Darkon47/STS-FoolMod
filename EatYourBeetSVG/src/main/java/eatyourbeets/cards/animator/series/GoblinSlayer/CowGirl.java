package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CowGirl extends AnimatorCard
{
    public static final String ID = Register(CowGirl.class, EYBCardBadge.Discard);

    public CowGirl()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Motivate();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        FetchFromPile fetchFromPile;

        if (upgraded)
        {
            fetchFromPile = new FetchFromPile(name, 1, p.drawPile, p.discardPile);
        }
        else
        {
            fetchFromPile = new FetchFromPile(name, 1, p.drawPile);
        }

        GameActions.Top.Add(fetchFromPile
        .SetOptions(false, false)
        .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c)));
    }
}