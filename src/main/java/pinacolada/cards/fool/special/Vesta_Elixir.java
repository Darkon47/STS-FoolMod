package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericCardEffect_GainOrbSlots;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericCardEffect_GainTempHP;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Vesta_Elixir extends FoolCard
{
    public static final PCLCardData DATA = Register(Vesta_Elixir.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColorless()
            .SetSeries(CardSeries.TenseiSlime);
    public static final int MAX_GROUP_SIZE = 3;
    public final ArrayList<GenericCardEffect> effects = new ArrayList<>();

    public Vesta_Elixir()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Star(1);
        SetPurge(true);
    }

    public Vesta_Elixir(ArrayList<GenericCardEffect> effects)
    {
        this();

        ApplyEffects(effects);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Vesta_Elixir other = (Vesta_Elixir) super.makeStatEquivalentCopy();

        other.ApplyEffects(effects);

        return other;
    }

    @Override
    public boolean canUpgrade()
    {
        return effects.isEmpty();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (GenericCardEffect effect : effects)
        {
            effect.Use(p, m, info);
        }
    }

    public void ResearchEffects() {
        effects.clear();
        final RandomizedList<GenericCardEffect> possibleEffects = new RandomizedList<>();
        for (PCLAffinity af : PCLAffinity.Extended()) {
            possibleEffects.Add(GenericCardEffect.GainAffinityPower(magicNumber, af));
        }
        possibleEffects.Add(GenericCardEffect.Gain(secondaryValue, PCLPowerHelper.Inspiration));
        possibleEffects.Add(GenericCardEffect.Gain(magicNumber, PCLPowerHelper.Malleable));
        possibleEffects.Add(GenericCardEffect.Gain(secondaryValue, PCLPowerHelper.Metallicize));
        possibleEffects.Add(GenericCardEffect.Gain(secondaryValue, PCLPowerHelper.Energized));
        possibleEffects.Add(new GenericCardEffect_GainTempHP(magicNumber * 2));
        possibleEffects.Add(new GenericCardEffect_GainOrbSlots(secondaryValue));

        ChooseEffect(possibleEffects);
    }

    public void ChooseEffect(RandomizedList<GenericCardEffect> possibleEffects) {
        if (effects.size() >= MAX_GROUP_SIZE) {
            return;
        }
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < MAX_GROUP_SIZE && possibleEffects.Size() > 0) {
            Vesta_Elixir other = (Vesta_Elixir) this.makeStatEquivalentCopy();
            other.ApplyEffect(possibleEffects.Retrieve(rng, true));
            group.addToTop(other);
        }

        PCLActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(c -> {
                    if (c.size() > 0) {
                        Vesta_Elixir elixir = PCLJUtils.SafeCast(c.get(0), Vesta_Elixir.class);
                        if (elixir != null) {
                            ApplyEffects(elixir.effects);
                            ChooseEffect(possibleEffects);
                        }
                    }
                });
    }

    protected void ApplyEffect(GenericCardEffect effect)
    {
        this.effects.add(effect);
        UpdateDescription();
    }

    protected void ApplyEffects(ArrayList<GenericCardEffect> effects)
    {
        this.effects.clear();
        this.effects.addAll(effects);
        UpdateDescription();
    }

    protected void UpdateDescription() {
        this.cardText.OverrideDescription(GenericCardEffect.JoinEffectTexts(effects), true);
    }
}