package pinacolada.resources.pcl;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.resources.common.CommonResources;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.events.base.PCLEvent;
import pinacolada.misc.CardPoolPanelItem;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.powers.replacement.GenericFadingPower;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.PCLAbstractResources;
import pinacolada.resources.PGR;
import pinacolada.rewards.pcl.ConcertsFinalHourReward;
import pinacolada.rewards.pcl.MissingPieceReward;
import pinacolada.rewards.pcl.SpecialGoldReward;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Field;

import static pinacolada.resources.PGR.Enums.Characters.THE_FOOL;

public class PCLResources extends PCLAbstractResources {
    public static final String ID = PGR.BASE_PREFIX;

    public final PCLDungeonData Dungeon = PCLDungeonData.Register(CreateID("Data"));
    public final PCLStrings Strings = new PCLStrings();
    public final PCLImages Images = new PCLImages();
    public final PCLConfig Config = new PCLConfig();
    protected String defaultLanguagePath;

    public PCLResources()
    {
        super(ID, AbstractCard.CardColor.COLORLESS, THE_FOOL);
    }

    protected void InitializeEvents()
    {
        PCLEvent.RegisterEvents();
    }

    protected void InitializeAudio()
    {
        SFX.Initialize();
    }

    protected void InitializeStrings()
    {
        PCLJUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(OrbStrings.class);
        LoadCustomCardStrings();
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

    protected void InitializeCards()
    {
        PCLJUtils.LogInfo(this, "InitializeCards();");

        PGR.Tooltips = new CardTooltips();
        Strings.Initialize();
        CardSeries.InitializeStrings();
        LoadCustomCards();
        PCLCardData.PostInitialize();
    }

    protected void InitializeRelics()
    {
        PCLJUtils.LogInfo(this, "InitializeRelics();");

        LoadCustomRelics();
    }

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

    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
        // LoadCustomPowers();
    }

    protected void InitializePotions()
    {
        PCLJUtils.LogInfo(this, "InitializePotions();");

        LoadCustomPotions();
    }

    protected void InitializeRewards()
    {
        MissingPieceReward.Serializer synergySerializer = new MissingPieceReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SERIES_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);

        ConcertsFinalHourReward.Serializer concertSerializer = new ConcertsFinalHourReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.BOSS_SERIES_CARDS, concertSerializer, concertSerializer);
    }

    protected void PostInitialize()
    {
        AttackEffects.Initialize();
        CommandsManager.RegisterCommands();
        PGR.Tooltips.InitializeIcons();
        PGR.UI.Initialize();
        Config.Load(CardCrawlGame.saveSlot);
        Config.InitializeOptions();
        BaseEffect.Initialize();
        PCLCustomCardSlot.Initialize();
        BaseMod.addTopPanelItem(new CardPoolPanelItem());
    }

    private static void AddEnergyTooltip(String symbol, TextureAtlas.AtlasRegion region)
    {
        if (region == null)
        {
            Texture texture = PGR.GetTexture(PGR.PCL.Images.ORB_C_PNG);
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

    public String CreateID(String suffix)
    {
        return CreateID(ID, suffix);
    }

    public void InitializeInternal()  { }

    protected void InitializeMonsters()  { }

    protected void InitializeTextures()  { }

}