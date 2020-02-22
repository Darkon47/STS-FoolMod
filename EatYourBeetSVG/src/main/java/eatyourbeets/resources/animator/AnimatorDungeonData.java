package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.events.animator.TheMaskedTraveler1;
import eatyourbeets.interfaces.subscribers.OnCardPoolChangedSubscriber;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashSet;

public class AnimatorDungeonData implements CustomSavable<AnimatorDungeonData>, StartGameSubscriber, StartActSubscriber
{
    public transient final ArrayList<AnimatorRuntimeLoadout> Series = new ArrayList<>();
    public transient AnimatorLoadout StartingSeries = new _FakeLoadout();
    public HashSet<String> BannedCards = new HashSet<>();

    protected ArrayList<AnimatorLoadoutProxy> loadouts = new ArrayList<>();
    protected int startingLoadout = -1;

    public static AnimatorDungeonData Register(String id)
    {
        AnimatorDungeonData data = new AnimatorDungeonData();
        BaseMod.addSaveField(id, data);
        BaseMod.subscribe(data);
        return data;
    }

    public void AddSeries(AnimatorRuntimeLoadout series)
    {
        Series.add(series);

        Log("Adding series: " + series.Loadout.Name);
    }

    public void AddAllSeries()
    {
        Series.clear();

        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            AnimatorRuntimeLoadout r = AnimatorRuntimeLoadout.TryCreate(loadout);
            if (r != null)
            {
                Series.add(r);
            }
        }

        FullLog("ADD ALL SERIES");
    }

    public void Reset()
    {
        FullLog("RESETTING...");

        Series.clear();
        StartingSeries = new _FakeLoadout();
        loadouts.clear();
        startingLoadout = -1;
    }

    @Override
    public AnimatorDungeonData onSave()
    {
        loadouts.clear();

        for (AnimatorRuntimeLoadout loadout : Series)
        {
            AnimatorLoadoutProxy surrogate = new AnimatorLoadoutProxy();
            surrogate.id = loadout.ID;
            surrogate.isBeta = loadout.IsBeta;
            surrogate.promoted = loadout.promoted;
            surrogate.bonus = loadout.bonus;
            loadouts.add(surrogate);
        }

        if (StartingSeries.ID > 0)
        {
            startingLoadout = StartingSeries.ID;
        }
        else
        {
            startingLoadout = GR.Animator.Data.SelectedLoadout.ID;
        }

        FullLog("ON SAVE");

        return this;
    }

    @Override
    public void onLoad(AnimatorDungeonData data)
    {
        Series.clear();
        BannedCards.clear();

        if (data != null)
        {
            BannedCards.addAll(data.BannedCards);
            StartingSeries = GR.Animator.Data.GetBaseLoadout(data.startingLoadout);

            for (AnimatorLoadoutProxy proxy : data.loadouts)
            {
                AnimatorRuntimeLoadout loadout = AnimatorRuntimeLoadout.TryCreate(GR.Animator.Data.GetLoadout(proxy.id, proxy.isBeta));
                if (loadout != null)
                {
                    if (proxy.promoted)
                    {
                        loadout.Promote();
                    }

                    loadout.bonus = proxy.bonus;
                    loadout.BuildCard();
                    Series.add(loadout);
                }
            }
        }

        if (StartingSeries == null)
        {
            StartingSeries = GR.Animator.Data.SelectedLoadout;
        }

        FullLog("ON LOAD");
    }

    @Override
    public void receiveStartAct()
    {
        FullLog("ON ACT START");
        InitializeCardPool(false);
    }

    @Override
    public void receiveStartGame()
    {
        FullLog("ON GAME START");
        InitializeCardPool(true);
    }

    public void InitializeCardPool(boolean startGame)
    {
        if (AbstractDungeon.player.chosenClass != GR.Animator.PlayerClass)
        {
            AbstractDungeon.srcColorlessCardPool.group.removeIf(c -> c instanceof AnimatorCard);
            AbstractDungeon.colorlessCardPool.group.removeIf(c -> c instanceof AnimatorCard);
            AbstractDungeon.eventList.remove(TheMaskedTraveler1.ID);

            return;
        }

        AbstractDungeon.bossRelicPool.remove(SneckoEye.ID);
        AbstractDungeon.bossRelicPool.remove(RunicPyramid.ID);
        AddRelicToPool(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);
        AddRelicToPool(RunicCapacitor.ID, AbstractRelic.RelicTier.SHOP);
        AddRelicToPool(TwistedFunnel.ID, AbstractRelic.RelicTier.SHOP);
        AddRelicToPool(Brimstone.ID, AbstractRelic.RelicTier.SHOP);
        AddRelicToPool(DataDisk.ID, AbstractRelic.RelicTier.SHOP);
        AddRelicToPool(CharonsAshes.ID, AbstractRelic.RelicTier.RARE);
        AddRelicToPool(ChampionsBelt.ID, AbstractRelic.RelicTier.RARE);
        AddRelicToPool(PaperCrane.ID, AbstractRelic.RelicTier.UNCOMMON);
        AddRelicToPool(PaperFrog.ID, AbstractRelic.RelicTier.UNCOMMON);
        AddRelicToPool(CloakClasp.ID, AbstractRelic.RelicTier.UNCOMMON);
        AddRelicToPool(RedSkull.ID, AbstractRelic.RelicTier.COMMON);

        if (startGame && Settings.isStandardRun())
        {
            GR.Animator.Data.SaveTrophies(true);
        }

        if (GameUtilities.GetActualAscensionLevel() >= 17)
        {
            AbstractDungeon.eventList.remove(Ghosts.ID);
        }

        if (Series.isEmpty())
        {
            return;
        }

        AbstractMissingPiece.RefreshDescription();
        PurgingStone.UpdateBannedCards();

        ArrayList<CardGroup> colorless = new ArrayList<>();
        colorless.add(AbstractDungeon.colorlessCardPool);
        colorless.add(AbstractDungeon.srcColorlessCardPool);
        for (CardGroup group : colorless)
        {
            group.group.removeIf(card -> !(card instanceof CustomCard) && !(card instanceof EYBCard));
        }

        ArrayList<CardGroup> groups = new ArrayList<>();
        groups.add(AbstractDungeon.commonCardPool);
        groups.add(AbstractDungeon.uncommonCardPool);
        groups.add(AbstractDungeon.srcCommonCardPool);
        groups.add(AbstractDungeon.srcUncommonCardPool);
        groups.add(AbstractDungeon.rareCardPool);
        groups.add(AbstractDungeon.srcRareCardPool);

        for (CardGroup group : groups)
        {
            group.group.removeIf(card ->
            {
                if (card.color != GR.Animator.CardColor)
                {
                    return false;
                }

                if (!BannedCards.contains(card.cardID))
                {
                    for (AnimatorRuntimeLoadout loadout : Series)
                    {
                        if (loadout.Cards.containsKey(card.cardID))
                        {
                            return false;
                        }
                    }
                }

                return true;
            });
        }

        if (AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null)
        {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
            {
                if (card instanceof OnCardPoolChangedSubscriber)
                {
                    ((OnCardPoolChangedSubscriber) card).OnCardPoolChanged();
                }
            }
        }
    }


    private void AddRelicToPool(String relicID, AbstractRelic.RelicTier tier)
    {
        if (!AbstractDungeon.player.hasRelic(relicID))
        {
            ArrayList<String> pool = null;

            switch (tier)
            {
                case COMMON:
                    pool = AbstractDungeon.commonRelicPool;
                    break;
                case UNCOMMON:
                    pool = AbstractDungeon.uncommonRelicPool;
                    break;
                case RARE:
                    pool = AbstractDungeon.rareRelicPool;
                    break;
                case BOSS:
                    pool = AbstractDungeon.bossRelicPool;
                    break;
                case SHOP:
                    pool = AbstractDungeon.shopRelicPool;
                    break;

                case DEPRECATED:
                case STARTER:
                case SPECIAL:
                    break;
            }

            if (pool != null && pool.size() > 0 && !pool.contains(relicID))
            {
                Random rng = AbstractDungeon.relicRng;
                if (rng == null)
                {
                    rng = GR.Common.Dungeon.GetRNG();
                }

                pool.add(rng.random(pool.size()-1), relicID);
            }
        }
    }

    private void Log(String message)
    {
        JavaUtilities.Log(this, message);
    }

    private void FullLog(String message)
    {
        JavaUtilities.Log(this, "================================================================================================");
        JavaUtilities.Log(this, message);
        JavaUtilities.Log(this, "[Transient  Data] Starting Series: " + StartingSeries.Name + ", Series Count: " + Series.size());
        JavaUtilities.Log(this, "[Persistent Data] Starting Series: " + startingLoadout + ", Series Count: " + loadouts.size());
    }

    protected static class AnimatorLoadoutProxy
    {
        public int id;
        public int bonus;
        public boolean isBeta;
        public boolean promoted;
    }
}