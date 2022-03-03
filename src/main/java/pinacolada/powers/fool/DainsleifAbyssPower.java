package pinacolada.powers.fool;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.cards.fool.colorless.Yoimiya;
import pinacolada.cards.fool.curse.Curse_AbyssalVoid;
import pinacolada.cards.fool.special.Ganyu;
import pinacolada.cards.fool.special.Traveler_Wish;
import pinacolada.cards.fool.tokens.AffinityToken;
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
        String combinedText = positiveEffect.text + " NL " + negativeEffect.effect.GetText();

        builder.SetText(NAME, combinedText, "");
        builder.SetProperties(AbstractCard.CardType.SKILL, PGR.Fool.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ALL);
        builder.SetOnUse((p, m, i) -> {
            positiveEffect.action.Invoke(p, m, i);
            negativeEffect.effect.Use(p, m, i);
        });

        return builder;
    }


    //TODO Refactor to use GenericEffect
    private enum AbyssNegativeEffect {
        DrawLessNextTurn(1, GenericCardEffect.DrawLessNextTurn(1)),
        EnemiesGainPlatedArmor(1, GenericCardEffect.ApplyToEnemies(1, PCLPowerHelper.PlatedArmor)),
        EnemiesGainStrength(2, GenericCardEffect.ApplyToEnemies(2, PCLPowerHelper.Strength)),
        EnemiesGainStrength2(3, GenericCardEffect.ApplyToEnemies(4, PCLPowerHelper.Strength)),
        EnemiesGainThorns(2, GenericCardEffect.ApplyToEnemies(4, PCLPowerHelper.Thorns)),
        EnemyRandomGainStrength(2, GenericCardEffect.ApplyToRandom(3, PCLPowerHelper.Strength)),
        EnemyRandomGainStrength2(3, GenericCardEffect.ApplyToRandom(6, PCLPowerHelper.Strength)),
        PlayerGainBurning(1, GenericCardEffect.Gain(2, PCLPowerHelper.Burning)),
        PlayerGainElectrified(3, GenericCardEffect.Gain(2, PCLPowerHelper.Electrified)),
        PlayerGainFrail(1, GenericCardEffect.Gain(2, PCLPowerHelper.Frail)),
        PlayerGainFreezing(1, GenericCardEffect.Gain(2, PCLPowerHelper.Freezing)),
        PlayerGainVulnerable(1, GenericCardEffect.Gain(1, PCLPowerHelper.Vulnerable)),
        PlayerGainVulnerable2(3, GenericCardEffect.Gain(3, PCLPowerHelper.Vulnerable)),
        PlayerGainWeak(1, GenericCardEffect.Gain(1, PCLPowerHelper.Weak)),
        PlayerGainWeak2(3, GenericCardEffect.Gain(3, PCLPowerHelper.Weak)),
        PlayerLoseDexterity(3, GenericCardEffect.Gain(-1, PCLPowerHelper.Dexterity)),
        PlayerLoseFocus(3, GenericCardEffect.Gain(-1, PCLPowerHelper.Focus)),
        PlayerLoseResistance(3, GenericCardEffect.Gain(-1, PCLPowerHelper.Resistance)),
        PlayerLoseStrength(3, GenericCardEffect.Gain(-1, PCLPowerHelper.Strength)),
        ObtainCurse(4, GenericCardEffect.Obtain(Curse_AbyssalVoid.DATA, Status_Burn.DATA));

        private final int tier;
        private final GenericCardEffect effect;

        AbyssNegativeEffect(int tier, GenericCardEffect effect) {
            this.tier = tier;
            this.effect = effect;
        }
    }

    private enum AbyssPositiveEffect {
        ApplyBlinded(ACTIONS.ApplyToALL(3, PGR.Tooltips.Blinded, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), 2)),
        ApplyBurning(ACTIONS.ApplyToALL(5, PGR.Tooltips.Burning, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), 4)),
        ApplyFreezing(ACTIONS.ApplyToALL(5, PGR.Tooltips.Freezing, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(), 4)),
        ApplyElectrified(ACTIONS.ApplyToALL(5, PGR.Tooltips.Electrified, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), 4)),
        ApplyVulnerable(ACTIONS.ApplyToALL(3, PGR.Tooltips.Vulnerable, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 3)),
        ApplyWeak(ACTIONS.ApplyToALL(3, PGR.Tooltips.Weak, true), 10, 1, (p, m, i) -> PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 3)),
        ChannelRandomOrbs(ACTIONS.ChannelRandomOrbs(2, true), 10, 2, (p, m, i) -> PCLActions.Bottom.ChannelRandomOrbs(2)),
        ChannelRandomOrbs2(ACTIONS.ChannelRandomOrbs(4, true), 10, 3, (p, m, i) -> PCLActions.Bottom.ChannelRandomOrbs(4)),
        NextTurnDraw(ACTIONS.NextTurnDraw(3, true), 10, 2, (p, m, i) -> PCLActions.Bottom.DrawNextTurn(3)),
        NextTurnEnergy(ACTIONS.NextTurnEnergy(2, true), 10, 2, (p, m, i) -> PCLActions.Bottom.GainEnergyNextTurn(2)),
        GainBlessing(ACTIONS.GainAmount(6, PGR.Tooltips.Invocation, true), 8, 2, (p, m, i) -> PCLActions.Bottom.GainInvocation(6, false)),
        GainCorruption(ACTIONS.GainAmount(9, PGR.Tooltips.Desecration, true), 7, 3, (p, m, i) -> PCLActions.Bottom.GainDesecration(9, false)),
        GainIntellect(ACTIONS.GainAmount(6, PGR.Tooltips.Wisdom, true), 8, 2, (p, m, i) -> PCLActions.Bottom.GainWisdom(6, false)),
        GainStrength(ACTIONS.GainAmount(4, PGR.Tooltips.Strength, true), 8, 4, (p, m, i) -> PCLActions.Bottom.GainStrength(4)),
        GainFocus(ACTIONS.GainAmount(3, PGR.Tooltips.Focus, true), 8, 4, (p, m, i) -> PCLActions.Bottom.GainFocus(3)),
        ObtainGenshinCard(ACTIONS.ChooseMotivatedCard(CardSeries.GenshinImpact.LocalizedName, true), 9, 3, (p, m, i) -> {
            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pool = new RandomizedList<AbstractCard>(genshinCards.GetInnerList());

            while (choice.size() < 3 && pool.Size() > 0)
            {
                AbstractCard ca = pool.Retrieve(rng).makeCopy();
                ca.upgrade();
                choice.addToTop(ca);
            }
            PCLActions.Last.SelectFromPile(null, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            PCLActions.Last.MakeCardInHand(cards.get(0))
                                    .AddCallback(ca -> ca.modifyCostForCombat(-1));
                        }
                    });
        }),
        ObtainTokenDark(ACTIONS.AddToDrawPile(2, AffinityToken_Dark.DATA.Strings.NAME, true), 7, 3, (p, m, i) -> PCLActions.Bottom.MakeCardInDrawPile(AffinityToken.GetCopy(PCLAffinity.Dark, false))),
        ObtainWish(ACTIONS.AddToDrawPile(2, Traveler_Wish.DATA.Strings.NAME, true), 6, 4, (p, m, i) -> PCLActions.Bottom.MakeCardInDrawPile(new Traveler_Wish()));



        private final String text;
        private final int weight;
        private final int tier;
        private final ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> action;

        AbyssPositiveEffect(String text, int weight, int tier, ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> action) {
            this.text = text;
            this.weight = weight;
            this.tier = tier;
            this.action = action;
        }
    }
}
