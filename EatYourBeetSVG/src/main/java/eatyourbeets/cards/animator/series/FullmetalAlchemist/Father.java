package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.listeners.OnAddedToDeckListener;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Father extends AnimatorCard implements OnAddedToDeckListener, OnAddingToCardRewardListener
{
    private static final AbstractRelic relic = new PhilosopherStone();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(relic.name, relic.description);

    public static final EYBCardData DATA = Register(Father.class).SetSkill(4, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(1);

    public Father()
    {
        super(DATA);

        Initialize(0, 0, 0, 50);
        SetUpgrade(0, 0, 0, -8);
        SetCostUpgrade(-1);

        SetHealing(true);
        SetPurge(true, false);
        SetSeries(CardSeries.FullmetalAlchemist);
        SetAffinity(0, 0, 2, 0, 2);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltip.id = cardID + ":" + tooltip.title;
            tooltips.add(tooltip);
        }
    }

    @Override
    public void OnAddedToDeck()
    {
        GR.Animator.Dungeon.Ban(cardData.ID);
        AbstractDungeon.bossRelicPool.remove(relic.relicId);
    }

    @Override
    public boolean ShouldCancel(RewardItem rewardItem)
    {
        GR.Animator.Dungeon.Ban(cardData.ID);

        return AbstractDungeon.actNum >= 4 || player == null || player.hasRelic(PhilosopherStone.ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!p.hasRelic(relic.relicId))
        {
            p.decreaseMaxHealth((int)Math.ceil(p.maxHealth * (secondaryValue / 100f)));
            GameActions.Bottom.VFX(new OfferingEffect(), 0.5f);
            GameActions.Bottom.Callback(() -> GameEffects.Queue.SpawnRelic(relic.makeCopy(), current_x, current_y));
            AbstractDungeon.bossRelicPool.remove(relic.relicId);

            p.energy.energy += 1;
        }

        //noinspection StatementWithEmptyBody
        while (p.masterDeck.removeCard(cardID));

        for (AbstractCard card : GameUtilities.GetAllInBattleCopies(cardID))
        {
            GameActions.Bottom.Purge(card);
        }
    }
}