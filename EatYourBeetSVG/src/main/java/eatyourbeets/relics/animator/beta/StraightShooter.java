package eatyourbeets.relics.animator.beta;

import eatyourbeets.cards.animator.beta.special.JumpyDumpty;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class StraightShooter extends AnimatorRelic
{
    public static final String ID = CreateFullID(StraightShooter.class);

    public StraightShooter()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onShuffle()
    {
        super.onShuffle();
        GameActions.Bottom.MakeCardInDrawPile(new JumpyDumpty())
                .AddCallback(card ->
                {
                    if (card instanceof EYBCard) {
                        card.upgrade();
                        card.applyPowers();
                    }

                });
        flash();
    }
}