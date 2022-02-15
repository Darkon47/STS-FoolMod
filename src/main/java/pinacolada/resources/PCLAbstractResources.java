package pinacolada.resources;

import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLCardMetadata;
import pinacolada.utilities.PCLJUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class PCLAbstractResources extends PGR
        implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
        EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
        AddAudioSubscriber
{
    public static final String JSON_CARDS = "CardStrings.json";
    public static final String JSON_KEYWORDS = "KeywordStrings.json";
    public static final String JSON_METADATA = "CardMetadata.json";
    public static final String JSON_SHORTCUTS = "CardStringsShortcuts.json";
    public Map<String, PCLCardMetadata> CardData;
    public final AbstractCard.CardColor CardColor;
    public final AbstractPlayer.PlayerClass PlayerClass;
    protected final FileHandle testFolder;
    protected final String prefix;
    protected String defaultLanguagePath;
    protected boolean isLoaded;

    protected PCLAbstractResources(String prefix, AbstractCard.CardColor color, AbstractPlayer.PlayerClass playerClass)
    {
        this.prefix = prefix;
        this.CardColor = color;
        this.PlayerClass = playerClass;
        this.testFolder = new FileHandle("c:/temp/" + prefix + "-localization/");
    }

    public String CreateID(String suffix)
    {
        return CreateID(prefix, suffix);
    }

    @Override
    public final void receiveEditCards()
    {
        InitializeCards();
    }

    @Override
    public final void receiveEditCharacters()
    {
        InitializeCharacter();
    }

    @Override
    public final void receiveEditKeywords()
    {
        InitializeKeywords();
    }

    @Override
    public final void receiveEditRelics()
    {
        InitializeRelics();
    }

    @Override
    public final void receiveEditStrings()
    {
        InitializeStrings();
    }

    @Override
    public final void receiveAddAudio()
    {
        InitializeAudio();
    }

    @Override
    public final void receivePostInitialize()
    {
        InitializeEvents();
        InitializeMonsters();
        InitializePotions();
        InitializeRewards();
        InitializePowers();
        PostInitialize();
        this.isLoaded = true;
    }

    protected void InitializeInternal()  { }
    protected void InitializeColor()     { }
    protected void InitializeMonsters()  { }
    protected void InitializeEvents()    { }
    protected void InitializeRewards()   { }
    protected void InitializeAudio()     { }
    protected void InitializeCards()     { }
    protected void InitializePowers()    { }
    protected void InitializeStrings()   { }
    protected void InitializeTextures()  { }
    protected void InitializeRelics()    { }
    protected void InitializePotions()   { }
    protected void InitializeCharacter() { }
    protected void InitializeKeywords()  { }
    protected void PostInitialize()      { }

    protected void LoadCustomCardStrings() {
        String json = GetFallbackFile(JSON_CARDS).readString(StandardCharsets.UTF_8.name());
        LoadGroupedCardStrings(ProcessCardJson(json, true));

        if (testFolder.isDirectory() || IsTranslationSupported(Settings.language))
        {
            FileHandle file = GetFile(Settings.language, JSON_CARDS);
            if (file.exists())
            {
                String json2 = file.readString(StandardCharsets.UTF_8.name());
                LoadGroupedCardStrings(ProcessCardJson(json2, false));
            }
        }
    }

    protected void LoadMetadata() {
        String jsonString = GetFile(Settings.language, JSON_METADATA).readString(String.valueOf(StandardCharsets.UTF_8));
        this.CardData = (new Gson()).fromJson(jsonString, (new TypeToken<Map<String, PCLCardMetadata>>() {
        }).getType());
    }

    public String ProcessCardJson(String originalString, boolean useFallback)
    {
        FileHandle file = useFallback ? GetFallbackFile(JSON_SHORTCUTS) : GetFile(Settings.language, JSON_SHORTCUTS);

        // Default to using the base mod shortcuts if a shortcut file for a character does not exist
        if (!file.exists())
        {
            file = useFallback ? PGR.PCL.GetFallbackFile(JSON_SHORTCUTS) : PGR.PCL.GetFile(Settings.language, JSON_SHORTCUTS);
            if (!file.exists()) {
                return originalString;
            }
        }

        String shortcutsJson = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, String> items = new Gson().fromJson(shortcutsJson, new TypeToken<Map<String, String>>(){}.getType());

        int size = items.size();
        String[] shortcuts = items.keySet().toArray(new String[size]);
        String[] replacement = items.values().toArray(new String[size]);

        return StringUtils.replaceEach(originalString, shortcuts, replacement);
    }

    public FileHandle GetFallbackFile(String fileName)
    {
        return Gdx.files.internal("localization/" + prefix.toLowerCase() + "/eng/" + fileName);
    }

    public <T> T GetFallbackStrings(String fileName, Type typeOfT)
    {
        FileHandle file = GetFallbackFile(fileName);
        if (!file.exists())
        {
            PCLJUtils.LogWarning(this, "File not found: " + file.path());
            return null;
        }

        String json = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }

    public boolean IsBetaTranslation()
    {
        return testFolder.isDirectory();
    }

    public FileHandle GetFile(Settings.GameLanguage language, String fileName)
    {
        if (IsBetaTranslation() && new File(testFolder.path() + "/" + fileName).isFile())
        {
            return Gdx.files.internal(testFolder.path() + "/" + fileName);
        }
        else
        {
            if (!IsTranslationSupported(language))
            {
                language = Settings.GameLanguage.ENG;
            }

            return Gdx.files.internal("localization/" + prefix.toLowerCase() + "/" + language.name().toLowerCase() + "/" + fileName);
        }
    }

    protected void LoadKeywords()
    {
        super.LoadKeywords(GetFallbackFile(JSON_KEYWORDS));

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadKeywords(GetFile(Settings.language, JSON_KEYWORDS));
        }
    }

    protected void LoadCustomPotions()
    {
        super.LoadCustomPotions(prefix);
    }

    protected void LoadCustomRelics()
    {
        super.LoadCustomRelics(prefix);
    }

    protected void LoadCustomCards()
    {
        super.LoadCustomCards(prefix);
    }

    protected void LoadCustomPowers()
    {
        super.LoadCustomPowers(prefix);
    }

    protected void LoadCustomStrings(Class<?> type)
    {
        super.LoadCustomStrings(type, GetFallbackFile(type.getSimpleName() + ".json"));

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadCustomStrings(type, GetFile(Settings.language, type.getSimpleName() + ".json"));
        }
    }
}
