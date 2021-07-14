package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardAffinityStatistics;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class SynergyCardsReward extends AnimatorReward
{
    public static final String ID = CreateFullID(SynergyCardsReward.class);

    public final CardSeries series;
    private boolean skip = false;
    private AnimatorRuntimeLoadout loadout;
    private EYBCardTooltip tooltip = new EYBCardTooltip("", "");

    private static String GenerateRewardTitle(CardSeries series)
    {
        if (series.ID == CardSeries.ANY.ID)
        {
            return "#yColorless";
        }
        else
        {
            return "#y" + series.LocalizedName.replace(" ", " #y");
        }
    }

    public SynergyCardsReward(CardSeries series)
    {
        super(ID, GenerateRewardTitle(series), GR.Enums.Rewards.SYNERGY_CARDS);

        this.series = series;
        this.cards = GenerateCardReward(series);
        this.loadout = GR.Animator.Dungeon.GetLoadout(series);
        this.tooltip.title = series.LocalizedName;
        this.tooltip.description = GR.Animator.Strings.Rewards.Description;
    }

    @Override
    public void update()
    {
        super.update();

        if (this.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip, 360 * Settings.scale, InputHelper.mY);
            //TipHelper.renderGenericTip(360f * Settings.scale, (float) InputHelper.mY, series.LocalizedName, GR.Animator.Strings.Rewards.Description);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        EYBCardAffinityStatistics statistics = null;
        if (series == CardSeries.ANY)
        {
            statistics = new EYBCardAffinityStatistics(AbstractDungeon.srcColorlessCardPool.group);
        }
        else if (loadout != null)
        {
            statistics = loadout.AffinityStatistics;
        }

        if (statistics == null)
        {
            return;
        }

        int max = AffinityType.BasicTypes().length;
        int borderLevel, i = 0, rendered = 0;
        float size, cX, cY;
        while (rendered < max)
        {
            EYBCardAffinityStatistics.Group group = statistics.GetGroup(i++);
            if (group == null)
            {
                return;
            }
            else if (group.Type == AffinityType.Star)
            {
                continue;
            }

            if (rendered < 2)
            {
                size = 42f;
                cX = hb.x + hb.width - ((2 - rendered) * size);
                cY = hb.y + (size * 0.615f);
                borderLevel = 2;
            }
            else
            {
                size = 28f;
                cX = hb.x + hb.width - ((max - rendered) * size * 1.14f);
                cY = hb.y + hb.height - (size * 0.6f);
                borderLevel = 0;
            }

            group.Render(sb, cX, cY, size, borderLevel);
            rendered += 1;
        }
    }

    @Override
    public boolean claimReward()
    {
        if (skip)
        {
            return true;
        }

        ArrayList<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
        int i = 0;
        while (i < rewards.size())
        {
            SynergyCardsReward other = JUtils.SafeCast(rewards.get(i), SynergyCardsReward.class);
            if (other != null && other != this)
            {
                other.isDone = true;
                other.skip = true;
            }
            i++;
        }

        if (AbstractDungeon.player.hasRelic(QuestionCard.ID))
        {
            AbstractDungeon.player.getRelic(QuestionCard.ID).flash();
        }

        if (AbstractDungeon.player.hasRelic(BustedCrown.ID))
        {
            AbstractDungeon.player.getRelic(BustedCrown.ID).flash();
        }

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        this.isDone = false;

        return false;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new SynergyCardsReward(CardSeries.GetByID(rewardSave.amount));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            SynergyCardsReward reward = JUtils.SafeCast(customReward, SynergyCardsReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, reward.series.ID, 0);
            }

            return null;
        }
    }
}