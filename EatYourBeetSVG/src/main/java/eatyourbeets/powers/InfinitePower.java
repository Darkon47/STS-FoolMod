package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.subscribers.OnBattleStartSubscriber;

public class InfinitePower extends AnimatorPower implements OnBattleStartSubscriber, OnApplyPowerSubscriber
{
    public static final String POWER_ID = CreateFullID(InfinitePower.class.getSimpleName());

    private final EnchantedArmorPower enchantedArmorPower;

    public InfinitePower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.enchantedArmorPower = new EnchantedArmorPower(owner, 0, true);

        this.priority = 100;
        this.amount = -1;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        OnBattleStart();

        GameActionsHelper.ApplyPowerSilently(owner, owner, enchantedArmorPower, 0);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer != owner.isPlayer)
        {
            return;
        }

        if (enchantedArmorPower.amount > 0)
        {
            enchantedArmorPower.amount = Math.max(1, enchantedArmorPower.amount / 2);
        }

        AbstractPower strengthPower = owner.getPower(StrengthPower.POWER_ID);
        if (strengthPower != null && strengthPower.amount > 0)
        {
            strengthPower.amount = Math.max(1, strengthPower.amount / 2);
        }

        AbstractPower regenPower = owner.getPower(RegenPower.POWER_ID);
        if (regenPower != null && regenPower.amount > 0)
        {
            regenPower.amount = Math.max(1, regenPower.amount / 2);
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (source != null && target != null)
        {
            int stacks = Math.abs(power.amount);
            if (source != owner && target == owner)
            {
                if (owner.isPlayer != source.isPlayer && power.type == PowerType.DEBUFF)
                {
                    GameActionsHelper.ApplyPower(owner, owner, new RegenPower(owner, stacks), stacks);
                }
            }
            else if (source != owner && target == source)
            {
                if (owner.isPlayer != source.isPlayer && power.type == PowerType.BUFF)
                {
                    GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, stacks), stacks);
                }
            }
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onBattleStart.Subscribe(this);
        PlayerStatistics.onApplyPower.Subscribe(this);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        int cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        if (cardsPlayed == 15)
        {
            GameActionsHelper.AddToBottom(new TalkAction(owner, "You are annoying me, I think your turn will end soon...", 1.5f, 1.5f));
        }
        else if (cardsPlayed == 20)
        {
            this.playApplyPowerSfx();
            AbstractDungeon.actionManager.cardQueue.clear();

            for (AbstractCard c : AbstractDungeon.player.limbo.group)
            {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }

            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        }
    }
}