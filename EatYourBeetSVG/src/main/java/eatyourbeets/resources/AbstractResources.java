package eatyourbeets.resources;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.Hidden;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResources implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
                                                   EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
                                                   AddAudioSubscriber
{
    public enum UIStringType
    {
        CharacterSelect,
        Synergies,
        Rewards,
        Actions,
        SpecialEffects,
        CardSelect,
        Trophies
    }

    protected static final Logger logger = LogManager.getLogger(Resources_Animator.class.getName());

    protected static Resources_Common commonResources;
    protected static Resources_Unnamed unnamedResources;
    protected static Resources_Animator animatorResources;

    public static CharacterStrings GetCharacterStrings(String characterID)
    {
        return CardCrawlGame.languagePack.getCharacterString(characterID);
    }

    public static MonsterStrings GetMonsterStrings(String monsterID)
    {
        return CardCrawlGame.languagePack.getMonsterStrings(monsterID);
    }

    public static CardStrings GetCardStrings(String cardID)
    {
        return CardCrawlGame.languagePack.getCardStrings(cardID);
    }

    public static EventStrings GetEventStrings(String eventID)
    {
        return CardCrawlGame.languagePack.getEventString(eventID);
    }

    public static OrbStrings GetOrbStrings(String orbID)
    {
        return CardCrawlGame.languagePack.getOrbString(orbID);
    }

    public static UIStrings GetUIStrings(UIStringType type) { return CardCrawlGame.languagePack.getUIString("animator:" + type.name()); }

    public static String GetCardImage(String cardID)
    {
        return "images/cards/" + cardID.replace(":", "/") + ".png";
    }

    public static String GetRelicImage(String relicID)
    {
        return "images/relics/" + relicID.replace(":", "/") + ".png";
    }

    public static String GetPowerImage(String powerID)
    {
        return "images/powers/" + powerID.replace(":", "/") + ".png";
    }

    public static String GetMonsterImage(String monsterID)
    {
        return "images/monsters/" + monsterID.replace(":", "/") + ".png";
    }

    public static String GetRewardImage(String rewardID)
    {
        return "images/ui/rewards/" + rewardID + ".png";
    }

    public static void Initialize()
    {
        commonResources = Initialize(commonResources, new Resources_Common());
        animatorResources = Initialize(animatorResources, new Resources_Animator());
        //unnamedResources = Initialize(unnamedResources, new Resources_Unnamed());
    }

    private static <T extends AbstractResources> T Initialize(AbstractResources existing, T resources)
    {
        if (existing != null)
        {
            BaseMod.unsubscribe(existing);
        }

        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);

        return resources;
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
    public void receiveAddAudio()
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
    }

    protected static void LoadCustomCards(String character)
    {
        // Do NOT localize this, it is used to load every card's ID
        String jsonString = Gdx.files.internal("localization/" + character +"/eng/CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, CardStrings> map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, CardStrings>>()
        {
        }.getType());

        for (String s : map.keySet())
        {
            try
            {
                logger.info("Adding: " + s);
                LoadCard(Class.forName("eatyourbeets.cards."+ character + "." + s.replace(character + ":", "")));
            }
            catch( ClassNotFoundException e )
            {
                logger.warn("Class not found : " + s);
            }
        }
    }

    protected static void LoadCard(Class<?> cardClass)
    {
        if (Hidden.class.isAssignableFrom(cardClass))
        {
            return;
        }

        AbstractCard card;
        String id;

        try
        {
            card = (AbstractCard) cardClass.getConstructor().newInstance();
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

        // animator_ -> animator:
        String oldID = id.replace(":", "_");
        if (UnlockTracker.seenPref.data.containsKey(oldID))
        {
            int res = UnlockTracker.seenPref.getInteger(oldID);

            UnlockTracker.seenPref.data.remove(oldID);

            if (res > 0)
            {
                UnlockTracker.seenPref.putInteger(id, 1);
                UnlockTracker.seenPref.flush();
            }
        }

        BaseMod.addCard(card);
    }

    protected static void LoadCustomPowers(String character)
    {
        String jsonString = Gdx.files.internal("localization/" + character +"/eng/PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, CardStrings> map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, CardStrings>>()
        {
        }.getType());

        for (String powerID : map.keySet())
        {
            try
            {
                logger.info("Adding: " + powerID);
                Class<? extends AbstractPower> powerClass = (Class<? extends AbstractPower>) Class.forName("eatyourbeets.powers."+ character + "." + powerID.replace(character + ":", ""));

                BaseMod.addPower(powerClass, powerID);
            }
            catch( ClassNotFoundException e )
            {
                logger.warn("Class not found : " + powerID);
            }
        }
    }

    protected static void LoadPower(Class<?> cardClass)
    {
        if (Hidden.class.isAssignableFrom(cardClass))
        {
            return;
        }

        AbstractCard card;
        String id;

        try
        {
            card = (AbstractCard) cardClass.getConstructor().newInstance();
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

        // animator_ -> animator:
        String oldID = id.replace(":", "_");
        if (UnlockTracker.seenPref.data.containsKey(oldID))
        {
            int res = UnlockTracker.seenPref.getInteger(oldID);

            UnlockTracker.seenPref.data.remove(oldID);

            if (res > 0)
            {
                UnlockTracker.seenPref.putInteger(id, 1);
                UnlockTracker.seenPref.flush();
            }
        }

        BaseMod.addCard(card);
    }

    protected void InitializeInternal()
    {
    }

    protected void InitializeColor()
    {
    }

    protected void InitializeMonsters()
    {
    }

    protected void InitializeEvents()
    {
    }

    protected void InitializeRewards()
    {
    }

    protected void InitializeAudio()
    {
    }

    protected void InitializeCards()
    {
    }

    protected void InitializePowers()
    {
    }

    protected void InitializeStrings()
    {
    }

    protected void InitializeRelics()
    {
    }

    protected void InitializePotions()
    {
    }

    protected void InitializeCharacter()
    {
    }

    protected void InitializeKeywords()
    {
    }

    protected void PostInitialize()
    {
    }
}
