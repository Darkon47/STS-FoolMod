package pinacolada.relics.fool;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.markers.Hidden;
import pinacolada.cards.base.PCLCard;
import pinacolada.relics.FoolRelic;
import pinacolada.utilities.PCLActions;

import static pinacolada.resources.PGR.Enums.CardTags.HARMONIC;


public class Headphones extends FoolRelic implements Hidden
{
    public static final String ID = CreateFullID(Headphones.class);

    public Headphones()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (IsEnabled() && drawnCard instanceof PCLCard && ((PCLCard) drawnCard).series != null) {
            PCLActions.Bottom.ModifyTag(drawnCard,HARMONIC,true);
            flash();
        }
    }

    @Override
    public void atTurnStartPostDraw()
    {
        if (IsEnabled()) {
            SetEnabled(false);
            flash();
        }
    }
}