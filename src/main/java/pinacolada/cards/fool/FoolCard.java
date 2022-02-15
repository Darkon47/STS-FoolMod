package pinacolada.cards.fool;

import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.PGR;

public class FoolCard extends PCLCard {

    protected FoolCard(PCLCardData cardData) {
        super(cardData);
    }

    protected FoolCard(PCLCardData cardData, int form, int timesUpgraded) {
        super(cardData, form, timesUpgraded);
    }

    protected static PCLCardData Register(Class<? extends PCLCard> type)
    {
        return RegisterCardData(type, PGR.Fool.CreateID(type.getSimpleName()))
                .SetResource(PGR.Fool);
    }
}
