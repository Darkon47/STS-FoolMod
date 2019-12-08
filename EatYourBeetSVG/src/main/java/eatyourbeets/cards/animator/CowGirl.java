package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

public class CowGirl extends AnimatorCard
{
    public static final String ID = Register(CowGirl.class.getSimpleName(), EYBCardBadge.Discard);

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
            GameActionsHelper2.Motivate();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        FetchFromPile action;
        if (upgraded)
        {
            action = new FetchFromPile(name, 1, p.drawPile, p.discardPile);
        }
        else
        {
            action = new FetchFromPile(name, 1, p.drawPile);
        }

        GameActionsHelper2.AddToTop(action
        .SetOptions(false, false)
        .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c)));
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}