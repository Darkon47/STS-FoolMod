package pinacolada.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLCardData;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.PGR;

public class EternalPower extends PCLPower {

    public static String CreateFullID(Class<? extends PCLPower> type) {
        return PGR.Eternal.CreateID(type.getSimpleName());
    }

    public EternalPower(AbstractCreature owner, AbstractCreature source, String id) {
        super(owner, source, id);
    }

    public EternalPower(AbstractCreature owner, String id) {
        super(owner, id);
    }

    public EternalPower(AbstractCreature owner, AbstractCreature source, PCLCardData cardData) {
        super(owner, source, cardData);
    }

    public EternalPower(AbstractCreature owner, PCLCardData cardData) {
        super(owner, cardData);
    }

    public EternalPower(AbstractCreature owner, AbstractCreature source, PCLRelic relic) {
        super(owner, source, relic);
    }

    public EternalPower(AbstractCreature owner, PCLRelic relic) {
        super(owner, relic);
    }
}
