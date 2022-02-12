package pinacolada.relics.fool;

import pinacolada.cards.base.PCLCard;
import pinacolada.cards.fool.special.JumpyDumpty;
import pinacolada.relics.FoolRelic;
import pinacolada.utilities.PCLActions;

public class StraightShooter extends FoolRelic
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
        PCLActions.Bottom.MakeCardInDrawPile(new JumpyDumpty())
                .AddCallback(card ->
                {
                    if (card instanceof PCLCard) {
                        card.upgrade();
                        card.applyPowers();
                    }

                });
        flash();
    }
}