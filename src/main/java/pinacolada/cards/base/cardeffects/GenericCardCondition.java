package pinacolada.cards.base.cardeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GenericCondition;
import pinacolada.cards.base.CardUseInfo;

public abstract class GenericCardCondition<T> extends GenericCardEffect {

    protected GenericCondition<CardUseInfo> condition;
    protected GenericCardEffect childEffect;

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (condition.Check(info)) {
            childEffect.Use(p, m, info);
        }
    }

}
