package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class CardEffectChoice
{
    protected final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final ArrayList<PCLCardBuilder> effects = new ArrayList<>();
    protected PCLCard source;

    public CardEffectChoice Initialize(PCLCard source)
    {
        return Initialize(source, false);
    }

    public CardEffectChoice Initialize(PCLCard source, boolean clearEffects)
    {
        this.source = source;

        if (clearEffects)
        {
            effects.clear();
            group.clear();
        }

        return this;
    }

    public boolean TryInitialize(PCLCard source)
    {
        if (this.source == null)
        {
            Initialize(source);
            return true;
        }

        return false;
    }

    public CardGroup Build(boolean forceRebuild)
    {
        if (group.isEmpty() || forceRebuild)
        {
            group.clear();

            for (PCLCardBuilder b : effects)
            {
                group.addToTop(b.Build());
            }
        }

        return group;
    }

    public PCLCardBuilder AddEffect(String text, ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> onUse)
    {
        PCLCardBuilder builder = new PCLCardBuilder(source, text, false).SetOnUse(onUse);
        effects.add(builder);
        return builder;
    }

    public PCLCardBuilder AddEffect(BaseEffect effect)
    {
        PCLCardBuilder builder = new PCLCardBuilder(source, effect.GetText(), false)
                .SetCardTarget(effect.target)
                .SetOnUse(effect::Use);
        if (effect.effectID != null)
        {
            builder.SetID(effect.effectID);
        }
        effects.add(builder);
        return builder;
    }

    public SelectFromPile Select(int amount, AbstractMonster m)
    {
        return Select(PCLActions.Bottom, Build(false), amount, m);
    }

    public SelectFromPile Select(PCLActions gameActions, int amount, AbstractMonster m)
    {
        return Select(gameActions, Build(false), amount, m);
    }

    public SelectFromPile Select(PCLActions gameActions, CardGroup group, int amount, AbstractMonster m)
    {
        return (SelectFromPile) gameActions.SelectFromPile(source.name, amount, group)
        .SetOptions(false, false)
        .AddCallback(m, (target, cards) ->
        {
            for (AbstractCard card : cards)
            {
                card.use(AbstractDungeon.player, target);
            }
        });
    }

    public SelectFromPile SelectWithTargeting(int amount)
    {
        return SelectWithTargeting(PCLActions.Bottom, Build(false), amount);
    }

    public SelectFromPile SelectWithTargeting(PCLActions gameActions, int amount)
    {
        return SelectWithTargeting(gameActions, Build(false), amount);
    }

    public SelectFromPile SelectWithTargeting(PCLActions gameActions, CardGroup group, int amount)
    {
        return (SelectFromPile) gameActions.SelectFromPile(source.name, amount, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        PCLActions.Top.SelectCreature(card).AddCallback(target -> {
                            card.use(AbstractDungeon.player, target instanceof AbstractMonster ? (AbstractMonster) target : null);
                        });
                    }
                });
    }

    public int Size() {
        return effects.size();
    }
}
