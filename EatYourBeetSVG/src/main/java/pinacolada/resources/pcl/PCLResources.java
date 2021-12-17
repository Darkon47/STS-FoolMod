package pinacolada.resources.pcl;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.resources.common.CommonResources;
import eatyourbeets.utilities.EYBFontHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.characters.FoolCharacter;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.events.base.PCLEvent;
import pinacolada.potions.GrowthPotion;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.powers.replacement.GenericFadingPower;
import pinacolada.resources.AbstractResources;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.rewards.pcl.MissingPieceReward;
import pinacolada.rewards.pcl.SpecialGoldReward;
import pinacolada.stances.PCLStance;
import pinacolada.ui.seriesSelection.PCLLoadoutsContainer;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PCLResources extends AbstractResources
{
    public final static String ID = GR.BASE_PREFIX;

    public final String OfficialName = "Animator (redesign)"; // Don't change this
    public final AbstractCard.CardColor CardColor = Enums.Cards.THE_FOOL;
    public final AbstractPlayer.PlayerClass PlayerClass = Enums.Characters.THE_FOOL;
    public final PCLDungeonData Dungeon = PCLDungeonData.Register(CreateID("Data"));
    public final PCLPlayerData Data = new PCLPlayerData();
    public final PCLStrings Strings = new PCLStrings();
    public final PCLImages Images = new PCLImages();
    public final PCLConfig Config = new PCLConfig();
    public Map<String, EYBCardMetadata> CardData;

    public PCLResources()
    {
        super(ID);
    }

    public int GetUnlockLevel()
    {
        return UnlockTracker.getUnlockLevel(PlayerClass);
    }

    public int GetUnlockCost()
    {
        return GetUnlockCost(0, true);
    }

    public int GetUnlockCost(int level, boolean relative)
    {
        if (relative)
        {
            level += GetUnlockLevel();
        }

        return level <= 4 ? 300 + (level * 500) : 1000 + (level * 300);
    }

    public boolean IsSelected()
    {
        return PCLGameUtilities.IsPlayerClass(PlayerClass);
    }

    @Override
    protected void InitializeEvents()
    {
        PCLEvent.RegisterEvents();
    }

    @Override
    protected void InitializeAudio()
    {
        SFX.Initialize();
    }

    @Override
    protected void InitializeStrings()
    {
        PCLJUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(OrbStrings.class);
        LoadCustomStrings(CharacterStrings.class);

        String json = GetFallbackFile("CardStrings.json").readString(StandardCharsets.UTF_8.name());
        LoadGroupedCardStrings(ProcessJson(json, true));

        if (testFolder.isDirectory() || IsTranslationSupported(Settings.language))
        {
            FileHandle file = GetFile(Settings.language, "CardStrings.json");
            if (file.exists())
            {
                String json2 = file.readString(StandardCharsets.UTF_8.name());
                LoadGroupedCardStrings(ProcessJson(json2, false));
            }
        }

        String jsonString = new String(Gdx.files.internal("PCL-CardMetadata.json").readBytes());
        CardData = new Gson().fromJson(jsonString, new TypeToken<Map<String, EYBCardMetadata>>(){}.getType());

        LoadCustomStrings(RelicStrings.class);
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
        LoadCustomStrings(EventStrings.class);
        LoadCustomStrings(PotionStrings.class);
        LoadCustomStrings(MonsterStrings.class);
        LoadCustomStrings(BlightStrings.class);
        LoadCustomStrings(RunModStrings.class);
        LoadCustomStrings(StanceStrings.class);

        EYBFontHelper.Initialize();
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(210, 106, 147);
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
        Images.ATTACK_PNG, Images.SKILL_PNG, Images.POWER_PNG,
        Images.ORB_A_PNG, Images.ATTACK_PNG_L, Images.SKILL_PNG_L,
        Images.POWER_PNG_L, Images.ORB_B_PNG, Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new FoolCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_JPG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        PCLJUtils.LogInfo(this, "InitializeCards();");

        GR.Tooltips = new CardTooltips();
        PCLStance.Initialize();
        Strings.Initialize();
        CardSeries.InitializeStrings();
        LoadCustomCards();
        PCLCardData.PostInitialize();
    }

    @Override
    protected void InitializeRelics()
    {
        PCLJUtils.LogInfo(this, "InitializeRelics();");

        LoadCustomRelics();
    }

    @Override
    protected void InitializeKeywords()
    {
        PCLJUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords();

        for (PCLAffinity affinity : PCLAffinity.Extended()) {
            AddAffinityTooltip(affinity);
        }
//        AddEnergyTooltip("[R]", AbstractCard.orb_red);
//        AddEnergyTooltip("[G]", AbstractCard.orb_green);
//        AddEnergyTooltip("[L]", AbstractCard.orb_blue);
//        AddEnergyTooltip("[P]", AbstractCard.orb_purple);
        AddEnergyTooltip("[E]", null); // TODO: generalize this

        for (Field field : GameDictionary.class.getDeclaredFields())
        {
            if (field.getType() == Keyword.class)
            {
                try
                {
                    final Keyword k = (Keyword) field.get(null);
                    final PCLCardTooltip tooltip = new PCLCardTooltip(PCLJUtils.Capitalize(k.NAMES[0]), k.DESCRIPTION);
                    CardTooltips.RegisterID(PCLJUtils.Capitalize(field.getName()), tooltip);

                    for (String name : k.NAMES)
                    {
                        CardTooltips.RegisterName(name, tooltip);
                    }
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
        // LoadCustomPowers();
    }

    @Override
    protected void InitializePotions()
    {
        BaseMod.addPotion(GrowthPotion.class, Color.NAVY.cpy(), Color.FOREST.cpy(), Color.YELLOW.cpy(),
        GrowthPotion.POTION_ID, Enums.Characters.THE_FOOL);
    }

    @Override
    protected void InitializeRewards()
    {
        MissingPieceReward.Serializer synergySerializer = new MissingPieceReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SERIES_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    @Override
    protected void PostInitialize()
    {
        AttackEffects.Initialize();
        CommandsManager.RegisterCommands();
        GR.Tooltips.InitializeIcons();
        GR.UI.Initialize();
        Config.Load(CardCrawlGame.saveSlot);
        Data.Initialize();
        Config.InitializeOptions();
        PCLLoadoutsContainer.PreloadResources();
        PCLImages.PreloadResources();
        GR.IsLoaded = true;
    }

    public String ProcessJson(String originalString, boolean useFallback)
    {
        final String path = "CardStringsShortcuts.json";
        final FileHandle file = useFallback ? GetFallbackFile(path) : GetFile(Settings.language, path);

        if (!file.exists())
        {
            return originalString;
        }

        String shortcutsJson = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, String> items = new Gson().fromJson(shortcutsJson, new TypeToken<Map<String, String>>(){}.getType());

        int size = items.size();
        String[] shortcuts = items.keySet().toArray(new String[size]);
        String[] replacement = items.values().toArray(new String[size]);

        return StringUtils.replaceEach(originalString, shortcuts, replacement);
    }

    private static void AddEnergyTooltip(String symbol, TextureAtlas.AtlasRegion region)
    {
        if (region == null)
        {
            Texture texture = GR.GetTexture(GR.PCL.Images.ORB_C_PNG);
            region = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            //region = new TextureAtlas.AtlasRegion(texture, 2, 2, texture.getWidth()-4, texture.getHeight()-4);
        }

        PCLCardTooltip tooltip = new PCLCardTooltip(TipHelper.TEXT[0], GameDictionary.TEXT[0]);
        tooltip.icon = region;
        CardTooltips.RegisterName(symbol, tooltip);
    }

    private static void AddAffinityTooltip(PCLAffinity affinity)
    {
        String symbol = affinity.GetFormattedPowerSymbol();
        String id = affinity.PowerName;
        AbstractPCLAffinityPower power = affinity.GetPower();

        if (power == null || power.img == null)
        {
            PCLJUtils.LogError(CommonResources.class, "Could not find image: Symbol: {0}, ID: {1}",
                    symbol, id);
            return;
        }
        int size = power.img.getWidth(); // width should always be equal to height

        PCLCardTooltip tooltip = CardTooltips.FindByID(id);
        if (tooltip == null)
        {
            PCLJUtils.LogError(CommonResources.class, "Could not find tooltip: Symbol: {0}, ID: {1}, Power: {2} ",
                    symbol, id, power.name);
            return;
        }

        tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 3, 5, size-6, size-6);
        //tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 2, 4, size-4, size-4);

        PCLCardTooltip stance = CardTooltips.FindByID(affinity.GetStanceTooltipID());
        if (stance != null)
        {
            stance.icon = tooltip.icon;
        }

        PCLCardTooltip scaling = CardTooltips.FindByID(affinity.GetScalingTooltipID());
        if (scaling != null)
        {
            scaling.icon = tooltip.icon;
        }

        CardTooltips.RegisterName(symbol, tooltip);
    }
}