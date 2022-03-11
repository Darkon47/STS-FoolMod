package pinacolada.resources.fool;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlock;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.RandomizedList;
import org.apache.commons.lang3.StringUtils;
import pinacolada.blights.common.AbstractGlyphBlight;
import pinacolada.blights.common.GlyphBlight;
import pinacolada.blights.common.GlyphBlight1;
import pinacolada.blights.common.GlyphBlight2;
import pinacolada.cards.base.CardSeries;
import pinacolada.resources.PCLAbstractPlayerData;
import pinacolada.resources.PGR;
import pinacolada.resources.fool.loadouts.*;
import pinacolada.resources.pcl.misc.*;
import pinacolada.ui.characterSelection.PCLBaseStatEditor;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import static pinacolada.ui.characterSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public class FoolPlayerData extends PCLAbstractPlayerData
{
    public static final int ASCENSION_GLYPH1_LEVEL_STEP = 2;
    public static final int ASCENSION_GLYPH1_UNLOCK = 18;

    public final int MajorVersion = 2;
    public final int MaxUnlockLevel = 8;
    public final ArrayList<PCLLoadout> BaseLoadouts = new ArrayList<>();
    public final ArrayList<PCLLoadout> BetaLoadouts = new ArrayList<>();
    public final ArrayList<PCLTrophies> Trophies = new ArrayList<>();
    public final ArrayList<AbstractGlyphBlight> Glyphs = new ArrayList<>();
    public PCLTrophies SpecialTrophies = new PCLTrophies(-1);
    public PCLLoadout SelectedLoadout = new _FakeLoadout();

    public void Initialize()
    {
        AddBaseLoadouts();
        AddBetaLoadouts();
        Reload();
    }

    public void Reload()
    {
        DeserializeTrophies(PGR.PCL.Config.Trophies.Get());
        DeserializeCustomLoadouts(PGR.PCL.Config.CustomLoadouts.Get());

        final Integer version = PGR.PCL.Config.MajorVersion.Get(null);
        if (version == null || version < MajorVersion)
        {
            UpdateMajorVersion(version);
        }

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = BaseLoadouts.get(0);
        }

        if (SpecialTrophies == null || SpecialTrophies.ID != 0)
        {
            SpecialTrophies = new PCLTrophies(0);
        }

        Glyphs.add(new GlyphBlight());
        Glyphs.add(new GlyphBlight1());
        Glyphs.add(new GlyphBlight2());
    }

    @Override
    public void InitializeCardPool(boolean startGame)
    {
        // The series may not be immediately initialized if the user starts a fresh save
        if (PGR.PCL.Config.SelectedSeries.Get() == null || PGR.PCL.Config.SelectedSeries.Get().size() < MINIMUM_SERIES) {
            PGR.PCL.Config.SelectedSeries.Set(PCLJUtils.Map(PGR.Fool.Data.GetEveryLoadout(), loadout -> loadout.Series), true);
        }

        // Always include the selected loadout. If for some reason none exists, assign one at random
        if (PGR.Fool.Data.SelectedLoadout == null) {
            PGR.Fool.Data.SelectedLoadout = PCLJUtils.Random(PCLJUtils.Filter(PGR.Fool.Data.GetEveryLoadout(), loadout -> PGR.Fool.GetUnlockLevel() >= loadout.UnlockLevel));
        }
        if (PGR.Fool.Data.SelectedLoadout != null) {
            final PCLRuntimeLoadout rloadout = PCLRuntimeLoadout.TryCreate(PGR.Fool.Data.SelectedLoadout);
            PGR.PCL.Dungeon.AddLoadout(rloadout);
            if (PGR.Fool.Data.SelectedLoadout.IsBeta)
            {
                Settings.seedSet = true;
            }
        }

        RandomizedList<PCLLoadout> rList = new RandomizedList<>(PGR.Fool.Data.GetEveryLoadout());
        int numberOfSeries = Mathf.Max(MINIMUM_SERIES, PGR.PCL.Config.SeriesSize.Get());
        while (PGR.PCL.Dungeon.Loadouts.size() < numberOfSeries && rList.Size() > 0) {
            PCLLoadout loadout = rList.Retrieve(GetRNG(), true);
            if ((PGR.Fool.Data.SelectedLoadout == null || !PGR.Fool.Data.SelectedLoadout.Series.equals(loadout.Series)) && PGR.PCL.Config.SelectedSeries.Get().contains(loadout.Series)) {
                if (loadout.IsBeta)
                {
                    // Do not unlock trophies or ascension
                    Settings.seedSet = true;
                }

                final PCLRuntimeLoadout rloadout = PCLRuntimeLoadout.TryCreate(loadout);
                // Series must be unlocked to be present in-game
                if (rloadout != null && !rloadout.isLocked) {
                    PGR.PCL.Dungeon.AddLoadout(rloadout);
                }
            }
        }

        PCLJUtils.LogInfo(this, "Starting Loadout: " + PGR.Fool.Data.SelectedLoadout.Series);
        PCLJUtils.LogInfo(this, "Starting Series: " + PCLJUtils.JoinStrings(",", PCLJUtils.Map(PGR.PCL.Dungeon.Loadouts, l -> l.Loadout.Series)));
    }

    public List<PCLLoadout> GetEveryLoadout()
    {
        return GetEveryLoadout(new ArrayList<>(BaseLoadouts.size() + BetaLoadouts.size()));
    }

    public List<PCLLoadout> GetEveryLoadout(List<PCLLoadout> list)
    {
        list.clear();
        list.addAll(BaseLoadouts);
        list.addAll(BetaLoadouts);
        return list;
    }

    public PCLLoadout GetByName(String name)
    {
        for (PCLLoadout loadout : GetEveryLoadout())
        {
            if (loadout.Name.equals(name))
            {
                return loadout;
            }
        }

        return null;
    }

    public PCLLoadout GetLoadout(CardSeries series)
    {
        if (series != null)
        {
            for (PCLLoadout loadout : GetEveryLoadout())
            {
                if (series.equals(loadout.Series))
                {
                    return loadout;
                }
            }
        }

        return null;
    }

    public PCLLoadout GetLoadout(int id)
    {
        final PCLLoadout loadout = GetBaseLoadout(id);
        if (loadout == null)
        {
            return GetBetaLoadout(id);
        }

        return loadout;
    }

    public PCLLoadout GetLoadout(int id, boolean isBeta)
    {
        return isBeta ? GetBetaLoadout(id) : GetBaseLoadout(id);
    }

    public PCLLoadout GetBetaLoadout(int id)
    {
        for (PCLLoadout loadout : BetaLoadouts)
        {
            if (loadout.ID == id)
            {
                return loadout;
            }
        }

        return null;
    }

    public PCLLoadout GetBaseLoadout(int id)
    {
        for (PCLLoadout loadout : BaseLoadouts)
        {
            if (loadout.ID == id)
            {
                return loadout;
            }
        }

        return null;
    }

    public PCLTrophies GetTrophies(int id)
    {
        for (PCLTrophies trophies : Trophies)
        {
            if (trophies.ID == id)
            {
                return trophies;
            }
        }

        return null;
    }

    public void RecordVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

        if (SelectedLoadout != null) {
            SelectedLoadout.OnVictory(ascensionLevel, 1);
        }

        SaveTrophies(true);
    }

    public void RecordTrueVictory(int ascensionLevel, int trophyLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

        boolean isUnnamedReign = PGR.PCL.Dungeon.IsUnnamedReign();

        if (isUnnamedReign)
        {
            if (SpecialTrophies.Trophy1 < 0)
            {
                SpecialTrophies.Trophy1 = 0;
            }

            SpecialTrophies.Trophy1 += 1 + Math.floorDiv(ascensionLevel, 4);
        }

        if (SelectedLoadout != null) {
            SelectedLoadout.OnVictory(ascensionLevel, trophyLevel);
        }

        SaveTrophies(true);
    }

    public void SaveTrophies(boolean flush)
    {
        PCLJUtils.LogInfo(FoolPlayerData.class, "Saving Trophies");

        PGR.PCL.Config.Trophies.Set(SerializeTrophies(), flush);
    }

    public void SaveLoadouts(boolean flush)
    {
        PCLJUtils.LogInfo(FoolPlayerData.class, "Saving Loadouts");

        PGR.PCL.Config.CustomLoadouts.Set(SerializeCustomLoadouts(), flush);
    }

    private void UpdateMajorVersion(Integer previousVersion)
    {
        PGR.PCL.Config.MajorVersion.Set(MajorVersion, true);
    }

    private void AddBaseLoadouts()
    {
        BaseLoadouts.clear();

        final ActionT2<PCLLoadout, Integer> add = (loadout, unlockLevel) ->
        {
            BaseLoadouts.add(loadout);
            loadout.IsBeta = false;
            loadout.UnlockLevel = unlockLevel;
            loadout.AddLoadoutCards();
        };

        add.Invoke(new Loadout_Konosuba(), 0);
        add.Invoke(new Loadout_GATE(), 0);
        add.Invoke(new Loadout_Elsword(), 0);
        add.Invoke(new Loadout_Katanagatari(), 0);
        add.Invoke(new Loadout_GoblinSlayer(), 0);
        add.Invoke(new Loadout_OwariNoSeraph(), 0);
        add.Invoke(new Loadout_Bleach(), 0);
        add.Invoke(new Loadout_FullmetalAlchemist(), 0);
        add.Invoke(new Loadout_Overlord(), 0);
        add.Invoke(new Loadout_OnePunchMan(), 0);
        add.Invoke(new Loadout_TenseiSlime(), 0);
        add.Invoke(new Loadout_LogHorizon(), 0);
        add.Invoke(new Loadout_Fate(), 1);
        add.Invoke(new Loadout_Rewrite(), 2);
        add.Invoke(new Loadout_NoGameNoLife(), 3);
        add.Invoke(new Loadout_DateALive(), 4);
        add.Invoke(new Loadout_TouhouProject(), 5);
        add.Invoke(new Loadout_GenshinImpact(), 5);
        add.Invoke(new Loadout_MadokaMagica(), 6);
        add.Invoke(new Loadout_AngelBeats(), 7);
        add.Invoke(new Loadout_RozenMaiden(), 7);

        for (PCLLoadout loadout : BaseLoadouts)
        {
            if (loadout.UnlockLevel <= 0)
            {
                continue;
            }

            final String cardID = loadout.GetSymbolicCard().ID;
            final CustomUnlock unlock = new CustomUnlock(AbstractUnlock.UnlockType.MISC, cardID);
            unlock.type = AbstractUnlock.UnlockType.CARD;
            unlock.card = new PCLRuntimeLoadout(loadout).BuildCard();
            unlock.key = unlock.card.cardID = PGR.PCL.CreateID("series:" + loadout.Name);

            CustomUnlockBundle bundle = BaseMod.getUnlockBundleFor(PGR.Fool.PlayerClass, loadout.UnlockLevel - 1);
            if (bundle == null)
            {
                bundle = new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "", "", "");
                bundle.getUnlocks().clear();
                bundle.getUnlockIDs().clear();
                bundle.getUnlockIDs().add(unlock.key);
                bundle.getUnlocks().add(unlock);
                bundle.unlockType = AbstractUnlock.UnlockType.CARD;
            }
            else
            {
                bundle.getUnlockIDs().add(unlock.key);
                bundle.getUnlocks().add(unlock);
            }

            BaseMod.addUnlockBundle(bundle, PGR.Fool.PlayerClass, loadout.UnlockLevel - 1);
        }
    }

    private void AddBetaLoadouts()
    {
        BetaLoadouts.clear();

        final ActionT2<PCLLoadout, Integer> add = (loadout, unlockLevel) ->
        {
            BetaLoadouts.add(loadout);
            loadout.IsBeta = true;
            loadout.UnlockLevel = unlockLevel;
            loadout.AddLoadoutCards();
        };

        //add.Invoke(new Loadout_HitsugiNoChaika(), 5);
        //add.Invoke(new Loadout_RozenMaiden(), 7);
    }

    // SelectedLoadout|Series_1,Trophy1,Trophy2,Trophy3|Series_2,Trophy1,Trophy2,Trophy3|...
    private String SerializeTrophies()
    {
        final StringJoiner sj = new StringJoiner("|");

        sj.add(String.valueOf(SelectedLoadout.ID));
        sj.add(SpecialTrophies.Serialize());

        for (PCLTrophies t : Trophies)
        {
            sj.add(t.Serialize());
        }

        return Base64Coder.encodeString(sj.toString());
    }

    private void DeserializeTrophies(String data)
    {
        Trophies.clear();
        SpecialTrophies = null;

        if (data != null && data.length() > 0)
        {
            final String decoded = Base64Coder.decodeString(data);
            final String[] items = PCLJUtils.SplitString("|", decoded);

            if (items.length > 0)
            {
                int loadoutID = PCLJUtils.ParseInt(items[0], -1);
                if (loadoutID > 0)
                {
                    SelectedLoadout = GetBaseLoadout(loadoutID);

                    if (SelectedLoadout == null)
                    {
                        SelectedLoadout = GetBetaLoadout(loadoutID);
                    }
                }

                for (int i = 1; i < items.length; i++)
                {
                    final PCLTrophies trophies = new PCLTrophies();

                    trophies.Deserialize(items[i]);

                    if (trophies.ID == 0)
                    {
                        SpecialTrophies = trophies;
                    }
                    else if (trophies.ID > 0)
                    {
                        Trophies.add(trophies);
                    }
                }
            }
        }
    }

    //003 _Gold@60;_HP@99;Strike@3:0;Defend@3:1;animator:Strike_Dark@1:2|004 Strike@4:0 ...
    private String SerializeCustomLoadouts()
    {
        final StringJoiner sj = new StringJoiner("|");
        final StringBuilder sb = new StringBuilder();

        final int level = PGR.Fool.GetUnlockLevel();
        for (PCLLoadout loadout : GetEveryLoadout())
        {
            if (loadout.UnlockLevel <= level)
            {
                for (PCLLoadoutData data : loadout.Presets)
                {
                    if (data == null)
                    {
                        continue;
                    }

                    sb.setLength(0);
                    sb.append(StringUtils.leftPad(String.valueOf(loadout.ID), 3, '0'))
                      .append(" ")
                      .append("_Preset@").append(data.Preset).append(":").append((data.Preset == loadout.Preset) ? 1 : 0).append(";")
                      .append("_Gold@").append(data.Values.getOrDefault(PCLBaseStatEditor.StatType.Gold, 0)).append(";")
                      .append("_HP@").append(data.Values.getOrDefault(PCLBaseStatEditor.StatType.HP, 0)).append(";")
                      .append("_OrbSlot@").append(data.Values.getOrDefault(PCLBaseStatEditor.StatType.OrbSlot, 0)).append(";")
                      .append("_PotionSlot@").append(data.Values.getOrDefault(PCLBaseStatEditor.StatType.PotionSlot, 0)).append(";")
                      .append("_CommonUpgrade@").append(data.Values.getOrDefault(PCLBaseStatEditor.StatType.CommonUpgrade, 0)).append(";");

                    for (PCLRelicSlot slot : data.relicSlots)
                    {
                        if (slot.selected != null)
                        {
                            sb.append("_Relic_").append(slot.GetRelic().relicId).append("@")
                                    .append(slot.GetSlotIndex()).append(";");
                        }
                    }

                    for (PCLCardSlot slot : data.cardSlots)
                    {
                        if (slot.amount > 0)
                        {
                            sb.append(slot.GetData().ID).append("@")
                              .append(slot.amount).append(":")
                              .append(slot.GetSlotIndex()).append(";");
                        }
                    }

                    sj.add(sb.toString());
                }
            }
        }

        return Base64Coder.encodeString(sj.toString());
    }

    private void DeserializeCustomLoadouts(String data)
    {
        if (StringUtils.isEmpty(data))
        {
            return;
        }

        final String decoded = Base64Coder.decodeString(data);
        final String[] strings = PCLJUtils.SplitString("|", decoded);
        for (String s : strings)
        {
            final int id = PCLJUtils.ParseInt(s.substring(0, 3), -1);
            final PCLLoadout loadout = GetLoadout(id);
            if (loadout == null)
            {
                PCLJUtils.LogWarning(this, "Loadout not found, ID:" + id);
                continue;
            }

            int i = 0;
            final PCLLoadoutData loadoutData = new PCLLoadoutData(loadout);
            for (String item : s.substring(4).split(Pattern.quote(";")))
            {
                final int index = item.indexOf("@");
                final String[] amountAndIndex = item.substring(index + 1).split(Pattern.quote(":"));
                final int itemAmount = PCLJUtils.ParseInt(amountAndIndex[0], 0);
                final int itemIndex = amountAndIndex.length > 1 ? PCLJUtils.ParseInt(amountAndIndex[1], -1) : -1;
                if (index == -1) {
                    PCLJUtils.LogWarning(this, "Loadout is corrupted, ID:" + id);
                    continue;
                }
                final String itemID = item.substring(0, index);
                switch (itemID)
                {
                    case "_Preset":
                        if (itemIndex == 1)
                        {
                            loadout.Preset = loadoutData.Preset = itemAmount;
                        }
                        else
                        {
                            loadoutData.Preset = itemAmount;
                        }
                        break;

                    case "_Gold":
                        loadoutData.Values.put(PCLBaseStatEditor.StatType.Gold, itemAmount);
                        break;

                    case "_HP":
                        loadoutData.Values.put(PCLBaseStatEditor.StatType.HP, itemAmount);
                        break;

                    case "_OrbSlot":
                        loadoutData.Values.put(PCLBaseStatEditor.StatType.OrbSlot, itemAmount);
                        break;

                    case "_PotionSlot":
                        loadoutData.Values.put(PCLBaseStatEditor.StatType.PotionSlot, itemAmount);
                        break;

                    case "_CommonUpgrade":
                        loadoutData.Values.put(PCLBaseStatEditor.StatType.CommonUpgrade, itemAmount);
                        break;

                    default:
                    {
                        if (itemID.startsWith("_Relic_")) {
                            // Relic indexes are stored in itemAmount instead of itemIndex
                            if (itemAmount < 0 || itemAmount >= loadoutData.CardsSize())
                            {
                                continue;
                            }
                            final String actualItemID = itemID.substring(7);
                            PCLJUtils.LogWarning(this, "GWARR, ID:" + id + " Item: " + actualItemID);
                            final PCLRelicSlot slot = loadoutData.GetRelicSlot(itemAmount);
                            for (PCLRelicSlot.Item c : slot.Relics)
                            {
                                if (c.relic.relicId.equals(actualItemID))
                                {
                                    slot.Select(c);
                                    break;
                                }
                            }
                        }
                        else {
                            if (itemIndex < 0 || itemIndex >= loadoutData.CardsSize())
                            {
                                continue;
                            }
                            final PCLCardSlot slot = loadoutData.GetCardSlot(itemIndex);
                            for (PCLCardSlot.Item c : slot.Cards)
                            {
                                if (c.data.ID.equals(itemID))
                                {
                                    slot.Select(c, itemAmount);
                                    break;
                                }
                            }
                        }

                        break;
                    }
                }
            }

            if (loadoutData.Validate().IsValid)
            {
                loadout.Presets[loadoutData.Preset] = loadoutData;
            }
        }
    }
}
