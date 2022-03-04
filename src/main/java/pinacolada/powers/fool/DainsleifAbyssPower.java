package pinacolada.powers.fool;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.colorless.Yoimiya;
import pinacolada.cards.fool.curse.Curse_AbyssalVoid;
import pinacolada.cards.fool.special.Ganyu;
import pinacolada.cards.fool.special.Traveler_Wish;
import pinacolada.cards.fool.tokens.AffinityToken_Dark;
import pinacolada.cards.fool.ultrarare.Dainsleif;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DainsleifAbyssPower extends PCLPower {
    private final static HashMap<Integer, RandomizedList<AbyssNegativeEffect>> negativeEffectList = new HashMap<>();
    private final static RandomizedList<AbstractCard> genshinCards = new RandomizedList<>();
    private final static WeightedList<AbyssPositiveEffect> positiveEffectList = new WeightedList<>();
    private static final PCLStrings.Actions ACTIONS = PGR.PCL.Strings.Actions;
    private static final String NAME = Dainsleif.DATA.Strings.NAME;
    public static final int COUNTDOWN_AMT = 24;
    public static final String POWER_ID = CreateFullID(DainsleifAbyssPower.class);
    public static final int CHOICES = 3;


    public DainsleifAbyssPower(AbstractCreature owner) {
        super(owner, POWER_ID);

        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = FormatDescription(0, COUNTDOWN_AMT);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        this.amount += 1 + card.cost;
        if (this.amount >= COUNTDOWN_AMT) {
            this.amount -= COUNTDOWN_AMT;
            this.playApplyPowerSfx();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            PCLActions.Bottom.VFX(new TimeWarpTurnEndEffect());
            PCLActions.Bottom.VFX(new BorderFlashEffect(Color.BLUE, true));
            PCLActions.Top.PlayFromPile(null, 1, null, player.drawPile, player.discardPile, player.hand).SetOptions(true, false).SetFilter(ca -> ca instanceof Dainsleif);
            ChooseEffect();
            PCLActions.Last.Add(new PressEndTurnButtonAction());
        }
        this.updateDescription();
    }

    private void InitializeLists() {
        if (genshinCards.Size() == 0) {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c instanceof PCLCard && ((PCLCard) c).series != null && ((PCLCard) c).series.Equals(CardSeries.GenshinImpact) &&
                        (c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE)) {
                    genshinCards.Add(c);
                }
            }
            genshinCards.Add(new Ganyu());
            genshinCards.Add(new Yoimiya());
        }

        if (negativeEffectList.size() == 0) {
            for (AbyssNegativeEffect effect : AbyssNegativeEffect.class.getEnumConstants()) {
                RandomizedList<AbyssNegativeEffect> list = negativeEffectList.getOrDefault(effect.tier, new RandomizedList<>());
                if (!negativeEffectList.containsKey(effect.tier)) {
                    negativeEffectList.put(effect.tier, list);
                }
                list.Add(effect);
            }
        }
        if (positiveEffectList.Size() == 0) {
            for (AbyssPositiveEffect effect : AbyssPositiveEffect.class.getEnumConstants()) {
                positiveEffectList.Add(effect, effect.weight);
            }
        }
    }

    private void ChooseEffect() {
        InitializeLists();

        WeightedList<AbyssPositiveEffect> tempPositiveEffects = new WeightedList<>(positiveEffectList);
        HashMap<Integer, RandomizedList<AbyssNegativeEffect>> tempNegativeEffects = new HashMap<>();
        for (Map.Entry<Integer,RandomizedList<AbyssNegativeEffect>> entry : negativeEffectList.entrySet()) {
            tempNegativeEffects.put(entry.getKey(), new RandomizedList<>(entry.getValue().GetInnerList()));
        }

        ArrayList<PCLCard_Dynamic> currentEffects = new ArrayList<>();

        while (currentEffects.size() < CHOICES) {
            AbyssPositiveEffect pEffect = tempPositiveEffects.Retrieve(rng);
            AbyssNegativeEffect nEffect = tempNegativeEffects.getOrDefault(pEffect.tier, new RandomizedList<>()).Retrieve(rng);
            if (nEffect != null) {
                PCLCardBuilder builder = Generate(pEffect, nEffect);
                currentEffects.add(builder.Build());
            }
            if (tempPositiveEffects.Size() == 0) {
                break;
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (PCLCard_Dynamic card : currentEffects) {
            if (card != null) {
                card.calculateCardDamage(null);
                group.addToTop(card);
            }
        }

        PCLActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (!cards.isEmpty()) {
                        cards.get(0).use(player, null);
                    }
                });
    }

    public PCLCardBuilder Generate(AbyssPositiveEffect positiveEffect, AbyssNegativeEffect negativeEffect) {
        PCLCardBuilder builder = new PCLCardBuilder(Dainsleif.DATA.ID + "Alt");
        String combinedText = positiveEffect.effect.GetText() + " NL " + negativeEffect.effect.GetText();

        builder.SetText(NAME, combinedText, "");
        builder.SetProperties(AbstractCard.CardType.SKILL, PGR.Fool.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ALL);
        builder.SetOnUse((p, m, i) -> {
            positiveEffect.effect.Use(p, m, i);
            negativeEffect.effect.Use(p, m, i);
        });

        return builder;
    }


    //TODO Refactor to use GenericEffect
    private enum AbyssNegativeEffect {
        DrawLessNextTurn(1, BaseEffect.Gain(1, PCLPowerHelper.NextTurnDrawLess)),
        EnemiesGainPlatedArmor(1, BaseEffect.ApplyToEnemies(1, PCLPowerHelper.PlatedArmor)),
        EnemiesGainStrength(2, BaseEffect.ApplyToEnemies(2, PCLPowerHelper.Strength)),
        EnemiesGainStrength2(3, BaseEffect.ApplyToEnemies(4, PCLPowerHelper.Strength)),
        EnemiesGainThorns(2, BaseEffect.ApplyToEnemies(4, PCLPowerHelper.Thorns)),
        EnemyRandomGainStrength(2, BaseEffect.ApplyToRandom(3, PCLPowerHelper.Strength)),
        EnemyRandomGainStrength2(3, BaseEffect.ApplyToRandom(6, PCLPowerHelper.Strength)),
        PlayerGainBurning(1, BaseEffect.Gain(2, PCLPowerHelper.Burning)),
        PlayerGainElectrified(3, BaseEffect.Gain(2, PCLPowerHelper.Electrified)),
        PlayerGainFrail(1, BaseEffect.Gain(2, PCLPowerHelper.Frail)),
        PlayerGainFreezing(1, BaseEffect.Gain(2, PCLPowerHelper.Freezing)),
        PlayerGainVulnerable(1, BaseEffect.Gain(1, PCLPowerHelper.Vulnerable)),
        PlayerGainVulnerable2(3, BaseEffect.Gain(3, PCLPowerHelper.Vulnerable)),
        PlayerGainWeak(1, BaseEffect.Gain(1, PCLPowerHelper.Weak)),
        PlayerGainWeak2(3, BaseEffect.Gain(3, PCLPowerHelper.Weak)),
        PlayerLoseDexterity(3, BaseEffect.Gain(-1, PCLPowerHelper.Dexterity)),
        PlayerLoseFocus(3, BaseEffect.Gain(-1, PCLPowerHelper.Focus)),
        PlayerLoseResistance(3, BaseEffect.Gain(-1, PCLPowerHelper.Resistance)),
        PlayerLoseStrength(3, BaseEffect.Gain(-1, PCLPowerHelper.Strength)),
        ObtainCurse(4, BaseEffect.Obtain(Curse_AbyssalVoid.DATA, Status_Burn.DATA));

        private final int tier;
        private final BaseEffect effect;

        AbyssNegativeEffect(int tier, BaseEffect effect) {
            this.tier = tier;
            this.effect = effect;
        }
    }

    private enum AbyssPositiveEffect {
        ApplyBlinded(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Blinded)),
        ApplyBurning(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Burning)),
        ApplyFreezing(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Freezing)),
        ApplyElectrified(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Electrified)),
        ApplyRippled(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Rippled)),
        ApplyVulnerable(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Vulnerable)),
        ApplyWeak(10, 1, BaseEffect.ApplyToEnemies(3, PCLPowerHelper.Weak)),
        ChannelRandomOrbs(10, 2, BaseEffect.ChannelOrb(2, null)),
        ChannelRandomOrbs2(10, 3, BaseEffect.ChannelOrb(4, null)),
        GainBluePower(10, 1, BaseEffect.GainAffinityPower(3, PCLAffinity.Blue)),
        GainDarkPower(10, 1, BaseEffect.GainAffinityPower(3, PCLAffinity.Dark)),
        GainLightPower(10, 1, BaseEffect.GainAffinityPower(3, PCLAffinity.Light)),
        GainSorcery(7, 3, BaseEffect.Gain(6, PCLPowerHelper.Sorcery)),
        GainStrength(5, 4, BaseEffect.Gain(3, PCLPowerHelper.Strength)),
        NextTurnDraw(10, 2, BaseEffect.Gain(2, PCLPowerHelper.NextTurnDraw)),
        NextTurnEnergy(10, 2, BaseEffect.Gain(2, PCLPowerHelper.Energized)),
        ObtainDarkToken(6, 3, BaseEffect.Obtain(AffinityToken_Dark.DATA)),
        ObtainWish(2, 4, BaseEffect.Obtain(Traveler_Wish.DATA));

        private final int weight;
        private final int tier;
        private final BaseEffect effect;

        AbyssPositiveEffect(int weight, int tier, BaseEffect effect) {
            this.weight = weight;
            this.tier = tier;
            this.effect = effect;
        }
    }
}
