package pinacolada.cards.base.cardeffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class CompositeCardEffect extends GenericCardEffect {
    protected final ArrayList<GenericCardEffect> effects;

    public CompositeCardEffect(GenericCardEffect... effects) {
        this.target = PCLJUtils.Max(effects, effect -> effect.target);
        this.effects = new ArrayList<>(Arrays.asList(effects));
        this.effectID = GenericCardEffect.JoinEntityIDs(effects, effect -> effect.entityID);
    }

    public CompositeCardEffect AddEffect(GenericCardEffect effect) {
        this.effects.add(effect);
        return this;
    }

    @Override
    public GenericCardEffect SetAmountFromCard() {
        super.SetAmountFromCard();
        for (GenericCardEffect effect : effects) {
            effect.SetAmountFromCard();
        }
        return this;
    }

    @Override
    public String GetText() {
        return GenericCardEffect.JoinEffectTexts(effects);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        for (GenericCardEffect effect : effects) {
            effect.Use(p, m, info);
        }
    }
}
