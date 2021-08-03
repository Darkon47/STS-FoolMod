package eatyourbeets.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.daily.mods.Binary;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public abstract class AnimatorReward extends CustomReward
{
    public static String CreateFullID(Class<? extends AnimatorReward> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public static float GetUltraRareChance(AnimatorLoadout loadout)
    {
        float bonus = 1;
        int level = GR.Animator.Data.SpecialTrophies.Trophy1;
        if (level > 0)
        {
            bonus += level / (level + 100f);
        }

        if (loadout == null)
        {
            return 7f * bonus;
        }
        else if (loadout.IsBeta)
        {
            return 6f * bonus;
        }
        else
        {
            return 4f * bonus;
        }
    }

    public AnimatorReward(String id, String text, RewardType type)
    {
        super(new Texture(GR.GetRewardImage(id)), text, type);
    }

    public AnimatorReward(Texture rewardImage, String text, RewardType type)
    {
        super(rewardImage, text, type);
    }

    public ArrayList<AbstractCard> GenerateCardReward(CardSeries series)
    {
        RewardContext context = new RewardContext(series);
        WeightedList<AbstractCard> randomPool = new WeightedList<>();
        if (series != null && series != CardSeries.ANY)
        {
            AddCards(AbstractDungeon.srcCommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcUncommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcRareCardPool, randomPool, context);
        }
        else
        {
            AddCards(AbstractDungeon.srcColorlessCardPool, randomPool, context);
        }

        ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < context.rewardSize && randomPool.Size() > 0)
        {
            AbstractCard card = randomPool.Retrieve(AbstractDungeon.cardRng);
            if (card instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener) card).ShouldCancel())
            {
                continue;
            }

            context.AddCard(card.makeCopy(), result);
        }

        if (result.size() > 0)
        {
            AddUltraRare(result, context.series);
        }

        return result;
    }

    private void AddCards(CardGroup pool, WeightedList<AbstractCard> cards, RewardContext context)
    {
        CardSeries series = context.series;

        for (AbstractCard c : pool.group)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null && (series.Equals(card.series) || CardSeries.ANY.equals(series)))
            {
                if (CardSeries.ANY.equals(series)) // colorless
                {
                    cards.Add(card, card.rarity == AbstractCard.CardRarity.UNCOMMON ? 8 : 2);
                }
                else if (series.equals(card.series))
                {
                    int weight = context.GetRarityWeight(card.rarity);
                    if (weight > 0)
                    {
                        cards.Add(card, weight);
                    }
                }
            }
        }
    }

    private void AddUltraRare(ArrayList<AbstractCard> cards, CardSeries series)
    {
        int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel <= 2 || AbstractDungeon.floorNum < 8 || AbstractDungeon.floorNum > 36 || cards.isEmpty())
        {
            return;
        }

        final AnimatorLoadout loadout = GR.Animator.Data.GetLoadout(series);
        float chances = GetUltraRareChance(loadout);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c instanceof AnimatorCard_UltraRare)
            {
                CardSeries s = ((AnimatorCard_UltraRare) c).series;
                if (s != null && series.ID == s.ID)
                {
                    return; // No duplicates
                }
                else
                {
                    chances *= 0.5f;
                }
            }
        }

        float roll = AbstractDungeon.cardRng.random(100f);
        if (roll < chances)
        {
            EYBCardData data = AnimatorCard_UltraRare.GetCardData(loadout);
            if (data != null)
            {
                cards.set(Math.min(1, cards.size() - 1), data.CreateNewInstance());
            }
        }
    }

    private static class RewardContext
    {
        public CardSeries series;
        public int rewardSize;
        public int rareCardChance;
        public int uncommonCardChance;
        public int commonCardChance;

        public RewardContext(CardSeries series)
        {
            this.series = series;
            this.rewardSize = 3;
            this.rareCardChance = 3;
            this.uncommonCardChance = 37;
            this.commonCardChance = 60;

            for (AbstractRelic relic : AbstractDungeon.player.relics)
            {
                this.rewardSize = relic.changeNumberOfCardsInReward(rewardSize);
                this.uncommonCardChance = relic.changeUncommonCardRewardChance(uncommonCardChance);
                this.rareCardChance = relic.changeRareCardRewardChance(rareCardChance);
            }

            if (ModHelper.isModEnabled(Binary.ID))
            {
                this.rewardSize -= 1;
            }
        }

        public int GetRarityWeight(AbstractCard.CardRarity rarity)
        {
            switch (rarity)
            {
                case COMMON:
                    return commonCardChance;

                case UNCOMMON:
                    return uncommonCardChance;

                case RARE:
                    return rareCardChance;

                default:
                    return 0;
            }
        }

        public void AddCard(AbstractCard card, ArrayList<AbstractCard> rewards)
        {
            for (AbstractRelic relic : AbstractDungeon.player.relics)
            {
                relic.onPreviewObtainCard(card);
            }

            rewards.add(card);
        }
    }
}