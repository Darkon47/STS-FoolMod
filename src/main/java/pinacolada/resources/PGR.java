package pinacolada.resources;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.RunModStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.GR;
import org.apache.logging.log4j.Logger;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.interfaces.markers.Replacement;
import pinacolada.relics.FoolReplacementRelic;
import pinacolada.resources.eternal.EternalResources;
import pinacolada.resources.fool.FoolResources;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PGR extends eatyourbeets.resources.GR
{
    public static final String BASE_PREFIX = "pcl";
    public static final String PREFIX_CARDS = "pinacolada.cards.";
    public static final String PREFIX_POTIONS = "pinacolada.potions.";
    public static final String PREFIX_POWERS = "pinacolada.powers.";
    public static final String PREFIX_RELIC = "pinacolada.relics.";

    protected static final Logger logger = PCLJUtils.GetLogger(PGR.class);
    protected static final ArrayList<String> cardClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_CARDS);
    protected static final ArrayList<String> potionClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_POTIONS);
    protected static final ArrayList<String> powerClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_POWERS);
    protected static final ArrayList<String> relicClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_RELIC);
    protected static final HashMap<String, Texture> localTextures = new HashMap();

    public static CardTooltips Tooltips = null;
    public static UIManager UI = new UIManager();
    public static PCLResources PCL;
    public static FoolResources Fool;
    public static EternalResources Eternal;

    public static boolean IsLoaded() {
        return PCL != null && PCL.isLoaded && Fool.isLoaded && Eternal.isLoaded;
    }

    public static void Initialize()
    {
        if (PCL != null)
        {
            throw new RuntimeException("Already Initialized");
        }

        PCL = new PCLResources();
        Fool = new FoolResources();
        Eternal = new EternalResources();

        Initialize(PCL);
        Initialize(Fool);
        Initialize(Eternal);
    }

    protected static void Initialize(PCLAbstractResources resources)
    {
        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);
    }

    public static RunModStrings GetRunModStrings(String stringID)
    {
        return CardCrawlGame.languagePack.getRunModString(stringID);
    }

    public static boolean IsTranslationSupported(Settings.GameLanguage language)
    {
        //This should be set only for beta branches.  Do not merge this into master.
        return false;//language == Settings.GameLanguage.RUS; // language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT;
    }

    public static Texture GetLocalTexture(String path) {
        return GetLocalTexture(path, false);
    }

    public static Texture GetLocalTexture(String path, boolean useMipMap) {
        return GetLocalTexture(path, true, false);
    }

    public static Texture GetLocalTexture(String path, boolean useMipMap, boolean refresh) {
        Texture texture = (Texture)localTextures.get(path);
        if (texture == null || refresh) {
            FileHandle file = Gdx.files.local(path);
            if (file.exists()) {
                texture = new Texture(file, useMipMap);
                if (useMipMap) {
                    texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
                } else {
                    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                }
            } else {
                PCLJUtils.GetLogger(GR.class).error("Texture does not exist: " + path);
            }

            localTextures.put(path, texture);
        }

        return texture;
    }

    protected void LoadCustomPotions(String character)
    {
        final String prefix = PREFIX_POTIONS + character;

        for (String s : potionClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCustomPotion(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadCustomPotion(Class<?> type)
    {
        if (!CanInstantiate(type))
        {
            return;
        }

        AbstractPotion potion;
        try
        {
            potion = (AbstractPotion)type.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID, Enums.Characters.THE_FOOL);
    }

    protected void LoadCustomRelics(String character)
    {
        final String prefix = PREFIX_RELIC + character;

        for (String s : relicClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCustomRelic(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadCustomRelic(Class<?> type)
    {
        if (!CanInstantiate(type))
        {
            return;
        }

        AbstractRelic relic;
        try
        {
            relic = (AbstractRelic)type.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        if (Replacement.class.isAssignableFrom(type)) {
            FoolReplacementRelic.RELICS.put(relic.relicId, relic);
            return;
        }

        BaseMod.addRelicToCustomPool(relic, Enums.Cards.THE_FOOL);
    }

    protected void LoadCustomCards(String character)
    {
        final String prefix = PREFIX_CARDS + character;
        for (String s : cardClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCustomCard(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadCustomCard(Class<?> type)
    {
        if (!CanInstantiate(type))
        {
            return;
        }

        AbstractCard card;
        String id;

        try
        {
            card = (AbstractCard)type.getConstructor().newInstance();
            id = card.cardID;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        if (UnlockTracker.isCardLocked(id))
        {
            UnlockTracker.unlockCard(id);
            card.isLocked = false;
        }

        BaseMod.addCard(card);
    }

    protected void LoadCustomPowers(String character)
    {
        final String prefix = PREFIX_CARDS + character;

        for (String s : cardClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    Class<?> type = Class.forName(s);
                    if (CanInstantiate(type))
                    {
                        BaseMod.addPower((Class<AbstractPower>) type, CreateID(character, type.getSimpleName()));
                    }
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadKeywords(FileHandle file)
    {
        if (!file.exists())
        {
            PCLJUtils.LogWarning(this, "File not found: " + file.path());
            return;
        }

        String json = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new Gson();
        Type typeToken = new TypeToken<Map<String, Keyword>>(){}.getType();
        Map<String, Keyword> items = gson.fromJson(json, typeToken);

        for (Map.Entry<String, Keyword> pair : items.entrySet())
        {
            String id = pair.getKey();
            Keyword keyword = pair.getValue();
            PCLCardTooltip tooltip = new PCLCardTooltip(keyword);

            CardTooltips.RegisterID(id, tooltip);

            for (String name : keyword.NAMES)
            {
                CardTooltips.RegisterName(name, tooltip);
            }
        }
    }

    public static class PackageNames {
        public static final String OG = "com.megacrit.cardcrawl";
        public static final String EYB_ANIMATOR = "eatyourbeets.cards.animator";
        public static final String EYB_UNNAMED = "eatyourbeets.cards.unnamed";
        public static final String PCL = "pinacolada";
        public static final String MARISA = "ThMod.cards.Marisa";
        public static final String GENSOKYO_ITEM = "Gensokyo.cards.Item";
        public static final String GENSOKYO_LUNAR = "Gensokyo.cards.Lunar";
        public static final String GENSOKYO_URBAN = "Gensokyo.cards.UrbanLegend";
    }

    public static class Enums extends eatyourbeets.resources.GR.Enums
    {
        public static class Characters extends eatyourbeets.resources.GR.Enums.Characters
        {
            @SpireEnum public static AbstractPlayer.PlayerClass THE_FOOL;
            @SpireEnum public static AbstractPlayer.PlayerClass THE_ETERNAL;
        }

        public static class Cards extends eatyourbeets.resources.GR.Enums.Cards
        {
            @SpireEnum public static AbstractCard.CardColor THE_FOOL;
            @SpireEnum public static AbstractCard.CardColor THE_ETERNAL;
        }

        public static class Library extends eatyourbeets.resources.GR.Enums.Library
        {
            @SpireEnum public static CardLibrary.LibraryType THE_FOOL;
            @SpireEnum public static CardLibrary.LibraryType THE_ETERNAL;
        }

        public static class Rewards extends eatyourbeets.resources.GR.Enums.Rewards
        {
            @SpireEnum public static RewardItem.RewardType SERIES_CARDS;
            @SpireEnum public static RewardItem.RewardType BOSS_SERIES_CARDS;
            @SpireEnum public static RewardItem.RewardType ENCHANTMENT;
        }

        public static class CardTags extends eatyourbeets.resources.GR.Enums.CardTags
        {
            @SpireEnum public static AbstractCard.CardTags AFTERLIFE;
            @SpireEnum public static AbstractCard.CardTags AUTOPLAY;
            @SpireEnum public static AbstractCard.CardTags EPHEMERAL;
            @SpireEnum public static AbstractCard.CardTags ETERNAL;
            @SpireEnum public static AbstractCard.CardTags EXPANDED;
            @SpireEnum public static AbstractCard.CardTags GRAVE;
            @SpireEnum public static AbstractCard.CardTags FRAGILE;
            @SpireEnum public static AbstractCard.CardTags HARMONIC;
            @SpireEnum public static AbstractCard.CardTags HASTE_INFINITE;
            @SpireEnum public static AbstractCard.CardTags PCL_ETHEREAL;
            @SpireEnum public static AbstractCard.CardTags PCL_EXHAUST;
            @SpireEnum public static AbstractCard.CardTags PCL_INNATE;
            @SpireEnum public static AbstractCard.CardTags PCL_RETAIN;
            @SpireEnum public static AbstractCard.CardTags PCL_RETAIN_ONCE;
            @SpireEnum public static AbstractCard.CardTags PCL_UNPLAYABLE;
            @SpireEnum public static AbstractCard.CardTags PROTAGONIST;
        }

        public static class AttackEffect extends eatyourbeets.resources.GR.Enums.AttackEffect
        {
            @SpireEnum public static AbstractGameAction.AttackEffect BLEED;
            @SpireEnum public static AbstractGameAction.AttackEffect CLASH;
            @SpireEnum public static AbstractGameAction.AttackEffect DARKNESS;
            @SpireEnum public static AbstractGameAction.AttackEffect ELECTRIC;
            @SpireEnum public static AbstractGameAction.AttackEffect FIRE_EXPLOSION;
            @SpireEnum public static AbstractGameAction.AttackEffect PSYCHOKINESIS;
            @SpireEnum public static AbstractGameAction.AttackEffect SMALL_EXPLOSION;
            @SpireEnum public static AbstractGameAction.AttackEffect SPARK;
            @SpireEnum public static AbstractGameAction.AttackEffect WATER;
            @SpireEnum public static AbstractGameAction.AttackEffect WAVE;
        }
    }
}
