package pinacolada.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLCardData;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.PGR;

public class FoolPower extends PCLPower {

    public static String CreateFullID(Class<? extends PCLPower> type) {
        return PGR.Fool.CreateID(type.getSimpleName());
    }

    public FoolPower(AbstractCreature owner, AbstractCreature source, String id) {
        super(owner, source, id);
    }

    public FoolPower(AbstractCreature owner, String id) {
        super(owner, id);
    }

    public FoolPower(AbstractCreature owner, AbstractCreature source, PCLCardData cardData) {
        super(owner, source, cardData);
    }

    public FoolPower(AbstractCreature owner, PCLCardData cardData) {
        super(owner, cardData);
    }

    public FoolPower(AbstractCreature owner, AbstractCreature source, PCLRelic relic) {
        super(owner, source, relic);
    }

    public FoolPower(AbstractCreature owner, PCLRelic relic) {
        super(owner, relic);
    }
}
