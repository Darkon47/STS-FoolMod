package pinacolada.resources.pcl;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLJUtils;

public class PCLStrings
{
    public Rewards Rewards;
    public Misc Misc;
    public Series Series;
    public SeriesUI SeriesUI;
    public CardEditor CardEditor;
    public CharacterSelect CharSelect;
    public SeriesSelection SeriesSelection;
    public SeriesSelectionButtons SeriesSelectionButtons;
    public SingleCardPopupButtons SingleCardPopupButtons;
    public Actions Actions;
    public Conditions Conditions;
    public CardTarget CardTarget;
    public Trophies Trophies;
    public Tutorial Tutorial;
    public EternalTutorial EternalTutorial;
    public Combat Combat;
    public Hotkeys Hotkeys;
    public CardMods CardMods;
    public TheUnnamedReign TheUnnamedReign;
    public HandSelection HandSelection;
    public GridSelection GridSelection;

    public void Initialize()
    {
        Misc = new Misc();
        Rewards = new Rewards();
        Series = new Series();
        SeriesUI = new SeriesUI();
        CardEditor = new CardEditor();
        CharSelect = new CharacterSelect();
        Actions = new Actions();
        Conditions = new Conditions();
        CardTarget = new CardTarget();
        Trophies = new Trophies();
        Tutorial = new Tutorial();
        EternalTutorial = new EternalTutorial();
        SeriesSelection = new SeriesSelection();
        SeriesSelectionButtons = new SeriesSelectionButtons();
        SingleCardPopupButtons = new SingleCardPopupButtons();
        Hotkeys = new Hotkeys();
        Combat = new Combat();
        CardMods = new CardMods();
        TheUnnamedReign = new TheUnnamedReign();
        HandSelection = new HandSelection();
        GridSelection = new GridSelection();
    }

    public static class Rewards
    {
        private final UIStrings Strings = GetUIStrings("Rewards");

        public final String Description = Strings.TEXT[0];
        public final String PossibleAffinities = Strings.TEXT[1];
        public final String CursedRelic = Strings.TEXT[2];
        public final String Reroll = Strings.TEXT[3];
        public final String MaxHPBonus_F1 = Strings.TEXT[4];
        public final String GoldBonus_F1 = Strings.TEXT[5];
        public final String CommonUpgrade = Strings.TEXT[6];
        public final String RightClickPreview = Strings.TEXT[7];
        public final String PotionSlot = Strings.TEXT[8];
        public final String OrbSlot = Strings.TEXT[9];

        public final String MaxHPBonus(int amount)
        {
            return PCLJUtils.Format(MaxHPBonus_F1, amount);
        }

        public final String GoldBonus(int amount)
        {
            return PCLJUtils.Format(GoldBonus_F1, amount);
        }
    }

    public static class Series
    {
        private final UIStrings Strings = GetUIStrings("Series");

        public final String Series = Strings.EXTRA_TEXT[0];
        public final String RandomSeries = Strings.EXTRA_TEXT[1];
        public final String Colorless = Strings.EXTRA_TEXT[2];

        public final String SeriesName(int seriesID)
        {
            return Strings.TEXT.length > seriesID ? Strings.TEXT[seriesID] : null;
        }
    }

    public static class SeriesUI
    {
        private final UIStrings Strings = GetUIStrings("SeriesUI");

        public final String SeriesUI = Strings.TEXT[0];
        public final String ItemsSelected = Strings.TEXT[1];
        public final String Keywords = Strings.TEXT[2];
        public final String Affinities = Strings.TEXT[3];
        public final String Scalings = Strings.TEXT[4];
        public final String Amount = Strings.TEXT[5];
        public final String Origins = Strings.TEXT[6];
        public final String Colors = Strings.TEXT[7];
        public final String Total = Strings.TEXT[8];
    }

    public static class CardEditor
    {
        private final UIStrings Strings = GetUIStrings("CardEditor");

        public final String Attributes = Strings.TEXT[0];
        public final String Effects = Strings.TEXT[1];
        public final String Tags = Strings.TEXT[2];
        public final String CardValue = Strings.TEXT[3];
        public final String NewCard = Strings.TEXT[4];
        public final String Damage = Strings.TEXT[5];
        public final String Block = Strings.TEXT[6];
        public final String MagicNumber = Strings.TEXT[7];
        public final String SecondaryNumber = Strings.TEXT[8];
        public final String HitCount = Strings.TEXT[9];
        public final String Upgrades = Strings.TEXT[10];
        public final String CardTarget = Strings.TEXT[11];
        public final String AttackType = Strings.TEXT[12];
        public final String UseBaseValue = Strings.TEXT[13];
        public final String PrimaryCondition = Strings.TEXT[14];
        public final String SecondaryCondition = Strings.TEXT[15];
        public final String Effect = Strings.TEXT[16];
        public final String EffectItems = Strings.TEXT[17];
        public final String EffectSource = Strings.TEXT[18];
        public final String Modifier = Strings.TEXT[19];
        public final String ModifierSource = Strings.TEXT[20];
    }

    public static class Misc
    {
        private final UIStrings Strings = GetUIStrings("Misc");

        public final String Discord = Strings.TEXT[0];
        public final String DiscordDescription = Strings.TEXT[1];
        public final String Steam = Strings.TEXT[2];
        public final String SteamDescription = Strings.TEXT[3];
        public final String NotEnoughCards = Strings.TEXT[4];
        public final String DynamicPortraits = Strings.TEXT[5];
        public final String UseCardHoveringAnimation = Strings.TEXT[6];
        public final String PressToCycle = Strings.TEXT[7];
        public final String LocalizationHelp = Strings.TEXT[8];
        public final String DisplayBetaSeries = Strings.TEXT[9];
        public final String LocalizationHelpHeader = Strings.TEXT[10];
        public final String FadeCardsWithoutSynergy = Strings.TEXT[11];
        public final String SimplifyCardUI = Strings.TEXT[12];
        public final String ViewCardPool = Strings.TEXT[13];
        public final String ViewCardPoolDescription = Strings.TEXT[14];
        public final String ViewCardPoolSeries = Strings.TEXT[15];
        public final String MaxStacks_F1 = Strings.TEXT[16];
        public final String GainBlockAboveMaxStacks_F1 = Strings.TEXT[17];
        public final String MaxBlock_F1 = Strings.TEXT[18];
        public final String EnableEventsForOtherCharacters = Strings.TEXT[19];
        public final String EnableRelicsForOtherCharacters = Strings.TEXT[20];
        public final String UnofficialDisclaimer = Strings.TEXT[21];
        public final String Filters = Strings.TEXT[22];
        public final String Any = Strings.TEXT[23];
        public final String ReplaceCardsFool = Strings.TEXT[24];
        public final String ReplaceCardsAnimator = Strings.TEXT[25];
        public final String EnableFlashForReroll = Strings.TEXT[26];

        public final String MaxStacks(int stacks)
        {
            return PCLJUtils.Format(MaxStacks_F1, stacks);
        }

        public final String GainBlockAboveMaxStacks(int stacks)
        {
            return PCLJUtils.Format(GainBlockAboveMaxStacks_F1, stacks);
        }

        public final String MaxBlock(int block)
        {
            return PCLJUtils.Format(MaxBlock_F1, block);
        }

        public final String PressKeyToCycle(String keyName) {
            return PCLJUtils.Format(PressToCycle, keyName);
        }
    }

    public static class CharacterSelect
    {
        private final UIStrings Strings = GetUIStrings("CharacterSelect");

        public final String LeftText = Strings.TEXT[0];  // Starting Cards:
        public final String RightText = Strings.TEXT[1]; // Unlock
        public final String InvalidLoadout = Strings.TEXT[3];
        public final String DeckEditor = Strings.TEXT[5];
        public final String DeckEditorInfo = Strings.TEXT[6];
        public final String SeriesEditor = Strings.TEXT[8];
        public final String SeriesEditorInfo = Strings.TEXT[9];
        public final String DeckHeader = Strings.TEXT[10];
        public final String RelicsHeader = Strings.TEXT[11];
        public final String AttributesHeader = Strings.TEXT[12];
        public final String ValueHeader = Strings.TEXT[13];
        public final String HindranceDescription = Strings.TEXT[18];
        public final String AffinityDescription = Strings.TEXT[19];
        public final String UnsavedChanges = Strings.TEXT[20];
        public final String Clear = Strings.TEXT[21];
        public final String CopyTo = Strings.TEXT[22];
        public final String CopyFrom = Strings.TEXT[23];
        public final String Export = Strings.TEXT[24];
        public final String AscensionGlyph = Strings.TEXT[25];
        public final String CardEditor = Strings.TEXT[26];
        public final String CardEditorInfo = Strings.TEXT[27];

        public final String UnlocksAtLevel(int unlockLevel, int currentLevel)
        {
            return PCLJUtils.Format(Strings.TEXT[2], unlockLevel, currentLevel);
        }

        public final String UnlocksAtAscension(int ascension)
        {
            return PCLJUtils.Format(Strings.TEXT[4], ascension);
        }

        public final String ObtainBronzeAtAscension(int ascension)
        {
            return PCLJUtils.Format(Strings.TEXT[7], ascension);
        }

        public final String HindranceValue(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[14], value);
        }

        public final String AffinityValue(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[15], value);
        }

        public final String CardsCount(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[16], value);
        }

        public final String TotalValue(int value, int max)
        {
            return PCLJUtils.Format(Strings.TEXT[17], value, max);
        }
    }

    public static class SeriesSelection
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelection");

        public final String Beta = Strings.TEXT[1];
        public final String TooltipBeta = Strings.TEXT[2];
        public final String RemoveFromPool = Strings.TEXT[3];
        public final String AddToPool = Strings.TEXT[4];
        public final String ViewPool = Strings.TEXT[5];
        public final String RightClickToPreview = Strings.TEXT[6];
        public final String ExpansionHeader = Strings.TEXT[7];
        public final String ExpansionCardBody = Strings.TEXT[8];
        public final String ExpansionSeriesUnlocked = Strings.TEXT[9];
        public final String ExpansionSeriesLocked = Strings.TEXT[10];
        public final String PoolSizeHeader = Strings.TEXT[11];
        public final String PoolSizeTip = Strings.TEXT[12];
        public final String SeriesSelectedCount = Strings.TEXT[13];
        public final String CardsSelectedCount = Strings.TEXT[14];

        public final String CardsSelected(Object cardCount)
        {
            return PCLJUtils.Format(CardsSelectedCount, cardCount);
        }

        public final String ContainsNCards(Object cardCount)
        {
            return PCLJUtils.Format(Strings.TEXT[0], cardCount);
        }

        public final String ContainsNCards_Full(Object cardCount, Object bronzeLevel, Object silverLevel, Object goldLevel)
        {
            return ContainsNCards(cardCount)
                    + " NL  NL " + PGR.Tooltips.Trophy1 + ": " + bronzeLevel
                    + " NL  NL " + PGR.Tooltips.Trophy2 + ": " + silverLevel
                    + " NL  NL " + PGR.Tooltips.Trophy3 + ": " + goldLevel;
        }

        public final String ContainsNCards_Beta(Object cardCount)
        {
            return ContainsNCards(cardCount) + " NL " + Strings.TEXT[1] + ".";
        }

        public final String SeriesSelected(Object cardCount)
        {
            return PCLJUtils.Format(SeriesSelectedCount, cardCount);
        }
    }

    public static class SeriesSelectionButtons
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelectionButtons");

        public final String ShowBetaSeries = Strings.TEXT[0];
        public final String DeselectAll = Strings.TEXT[1];
        public final String SelectAll = Strings.TEXT[2];
        public final String SelectRandom = Strings.TEXT[3];
        public final String ShowCardPool = Strings.TEXT[4];
        public final String Save = Strings.TEXT[5];
        public final String EnableExpansion = Strings.TEXT[6];
        public final String DisableExpansion = Strings.TEXT[7];
        public final String AllExpansionEnable = Strings.TEXT[8];
        public final String AllExpansionDisable = Strings.TEXT[9];
        public final String Cancel = Strings.TEXT[10];
        public final String ShowColorless = Strings.TEXT[11];

        public final String SelectRandom(int cards)
        {
            return PCLJUtils.Format(Strings.TEXT[2], cards) ;
        }
    }

    public static class SingleCardPopupButtons
    {
        private final UIStrings Strings = GetUIStrings("SingleCardPopupButtons");

        public final String Variant = Strings.TEXT[0];
        public final String ChangeVariant = Strings.TEXT[1];
        public final String ChangeVariantTooltipPermanent = Strings.TEXT[2];
        public final String ChangeVariantTooltipAlways = Strings.TEXT[3];
        public final String CurrentCopies = Strings.TEXT[4];
        public final String MaxCopies = Strings.TEXT[5];
        public final String MaxCopiesTooltip = Strings.TEXT[6];
        public final String ArtAuthor = Strings.TEXT[7];
    }

    public static class Hotkeys
    {
        private final UIStrings Strings = GetUIStrings("Hotkeys");

        public final String ControlPileChange = Strings.TEXT[0];
        public final String ControlPileSelect = Strings.TEXT[1];
        public final String Cycle = Strings.TEXT[2];
        public final String RerollCurrent = Strings.TEXT[3];
        public final String RerollNext = Strings.TEXT[4];
    }

    public static class Combat
    {
        private final UIStrings Strings = GetUIStrings("Combat");

        public final String Current = Strings.TEXT[0];
        public final String Next = Strings.TEXT[1];
        public final String Uses = Strings.TEXT[2];
        public final String Rerolls = Strings.TEXT[3];
        public final String Experience = Strings.TEXT[4];
        public final String ControlPile = Strings.TEXT[5];
        public final String ControlPileDescription = Strings.TEXT[6];
        public final String TotalMatches = Strings.TEXT[7];
        public final String CurrentMatchCombo = Strings.TEXT[8];
        public final String LongestMatchCombo = Strings.TEXT[9];
        public final String ResolveMeter = Strings.TEXT[10];
        public final String ResolveMeterDescription1 = Strings.TEXT[11];
        public final String ResolveMeterDescription2 = Strings.TEXT[12];
        public final String ResolveMeterDescription3 = Strings.TEXT[13];

        public final String ControlPileDescriptionFull(String keyName) {
            return PCLJUtils.Format(ControlPileDescription, keyName);
        }

        public final String ResolveMeterDescriptionFull(int curResolve, int maxResolve, int resolveGain, boolean isActive) {
            return PCLJUtils.Format(ResolveMeterDescription1, curResolve, maxResolve, resolveGain, isActive ? ResolveMeterDescription3 : curResolve >= maxResolve ? ResolveMeterDescription2 : "");
        }
    }

    public static class CardMods
    {
        private final UIStrings Strings = GetUIStrings("CardMods");

        public final String HandSize = Strings.TEXT[0];
        public final String AfterlifeMet = Strings.TEXT[1];
        public final String AfterlifeRequirement = Strings.TEXT[2];
        public final String RespecLivingPicture = Strings.TEXT[3];
        public final String RespecLivingPictureLocked = Strings.TEXT[4];
        public final String RespecLivingPictureDescription = Strings.TEXT[5];
        public final String Kirby = Strings.TEXT[6];
        public final String KirbyDescription = Strings.TEXT[7];
    }

    public static class CardTarget
    {
        private final UIStrings Strings = GetUIStrings("CardTarget");

        public final String None = Strings.TEXT[0];
        public final String AoE = Strings.TEXT[1];
        public final String All = Strings.TEXT[2];
        public final String Self = Strings.TEXT[3];
        public final String Normal = Strings.TEXT[4];
        public final String Any = Strings.TEXT[5];
        public final String Random = Strings.TEXT[6];
        public final String Ally = Strings.TEXT[7];
    }

    public static class Actions
    {
        private final UIStrings Strings = GetUIStrings("Actions");

        public final String Enemy = Strings.EXTRA_TEXT[0];
        public final String RandomEnemy = Strings.EXTRA_TEXT[1];
        public final String ALLEnemies = Strings.EXTRA_TEXT[2];
        public final String XCards = Strings.EXTRA_TEXT[3];
        public final String XRandomCards = Strings.EXTRA_TEXT[4];
        public final String ALLCards = Strings.EXTRA_TEXT[5];
        public final String DiscardPile = Strings.EXTRA_TEXT[6];
        public final String DrawPile = Strings.EXTRA_TEXT[7];
        public final String ExhaustPile = Strings.EXTRA_TEXT[8];
        public final String Hand = Strings.EXTRA_TEXT[9];
        public final String Anywhere = Strings.EXTRA_TEXT[10];

        public final String Cards(Object amount) {
            return PCLJUtils.Format(XCards, amount);
        }
        public final String RandomCards(Object amount) {
            return PCLJUtils.Format(XRandomCards, amount);
        }


        public final String AddToPile(Object desc1, Object desc2, Object pile, boolean addPeriod)
        {
            return Format(addPeriod, 0, desc1, desc2, pile);
        }
        public final String ApplyToTarget(Object power, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 1, power, target);
        }
        public final String ApplyAmountToTarget(Integer amount, Object power, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 2, amount, power, target);
        }
        public final String ApplyAmount(Integer amount, Object power, boolean addPeriod) {return Format(addPeriod, 3, amount, power);}
        public final String Apply(Object power, boolean addPeriod)
        {
            return Format(addPeriod, 4, power);
        }
        public final String ChannelRandomOrbs(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 5, amount);
        }
        public final String Channel(Object amount, Object orb, boolean addPeriod)
        {
            return Format(addPeriod, 6, amount, orb);
        }
        public final String ChooseOutOf(Integer amount, Integer amount2, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 7, amount, amount2, target);
        }
        public final String Choose(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 8, amount);
        }
        public final String CycleTarget(Object amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 9, amount, target);
        }
        public final String Cycle(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 10, amount);
        }
        public final String DealDamageTo(Integer amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 11, amount, target);
        }
        public final String DealDamage(Integer amount, boolean addPeriod)
        {
            return Format(addPeriod, 12, amount);
        }
        public final String DealTypeDamageTo(Object type, Integer amount, Object target, boolean addPeriod) {return Format(addPeriod, 13, type, amount, target);}
        public final String DealTypeDamage(Object type, Integer amount, boolean addPeriod)
        {
            return Format(addPeriod, 14, type, amount);
        }
        public final String DiscardFrom(Object amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 15, amount, target);
        }
        public final String Discard(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 16, amount);
        }
        public final String DrawType(Integer amount, Object type, boolean addPeriod)
        {
            return Format(addPeriod, 17, amount, type);
        }
        public final String Draw(Integer amount, boolean addPeriod)
        {
            return Format(addPeriod, 18, amount);
        }
        public final String EnterAnyStance(boolean addPeriod)
        {
            return Format(addPeriod, 19);
        }
        public final String EnterStance(Object stance, boolean addPeriod)
        {
            return Format(addPeriod, 20, stance);
        }
        public final String EvokeXTimes(Object orb, Integer times, boolean addPeriod)
        {
            return Format(addPeriod, 21, orb, times);
        }
        public final String Evoke(Object orb, Integer times, boolean addPeriod)
        {
            return Format(addPeriod, 22, orb, times);
        }
        public final String ExhaustFrom(Object amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 23, amount, target);
        }
        public final String Exhaust(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 24, amount);
        }
        public final String ExitStance(boolean addPeriod)
        {
            return Format(addPeriod, 25);
        }
        public final String FetchFrom(Object item, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 26, item, target);
        }
        public final String GainAmount(Integer amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 27, amount, power);
        }
        public final String Gain(Object power, boolean addPeriod)
        {
            return Format(addPeriod, 28, power);
        }
        public final String GiveTargetAmount(Object target, Integer amount, Object power, boolean addPeriod) {return Format(addPeriod, 29, target, amount, power);}
        public final String GiveTarget(Object target, Object power, boolean addPeriod) {return Format(addPeriod, 30, target, power);}
        public final String HaveTemporaryAmount(Integer amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 31, amount, power);
        }
        public final String Heal(Integer amount, boolean addPeriod)
        {
            return Format(addPeriod, 32, amount);
        }
        public final String LoseAmount(Integer amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 33, amount, power);
        }
        public final String MotivateFrom(Object amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 34, amount, target);
        }
        public final String Motivate(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 35, amount);
        }
        public final String MoveTo(Object amount, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 36, amount, target);
        }
        public final String ObtainOutOf(Integer amount, Integer amount2, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 37, amount, amount2, target);
        }
        public final String ObtainAmount(Integer amount, Object card, boolean addPeriod)
        {
            return Format(addPeriod, 38, amount, card);
        }
        public final String Obtain(Object card, boolean addPeriod)
        {
            return Format(addPeriod, 39, card);
        }
        public final String Pay(Integer amount, Object power, boolean addPeriod) {return Format(addPeriod, 40, amount, power);}
        public final String PlayFrom(Object item, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 41, item, target);
        }
        public final String Play(Object item, boolean addPeriod)
        {
            return Format(addPeriod, 42, item);
        }
        public final String RemoveCommonDebuffs(boolean addPeriod)
        {
            return Format(addPeriod, 43);
        }
        public final String Remove(Object item, boolean addPeriod)
        {
            return Format(addPeriod, 44, item);
        }
        public final String RemoveFrom(Object item, Object target, boolean addPeriod)
        {
            return Format(addPeriod, 45, item, target);
        }
        public final String RetainAmount(Integer amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 46, amount, power);
        }
        public final String Retain(Object power, boolean addPeriod)
        {
            return Format(addPeriod, 47, power);
        }
        public final String Scry(Integer amount, boolean addPeriod) {return Format(addPeriod, 48, amount);}
        public final String SetCurrentAffinity(Object affinity, boolean addPeriod) {return Format(addPeriod, 49, affinity);}
        public final String SetNextAffinity(Object affinity, boolean addPeriod) {return Format(addPeriod, 50, affinity);}
        public final String StealFrom(Integer amount, Object item, Object target, boolean addPeriod) {return Format(addPeriod, 51, amount, item, target);}
        public final String StealAmount(Integer amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 52, amount, power);
        }
        public final String Stun(Object target, boolean addPeriod)
        {
            return Format(addPeriod, 53, target);
        }
        public final String TakeDelayedDamage(Integer amount, boolean addPeriod) {return Format(addPeriod, 54, amount);}
        public final String TakeDamage(Integer amount, boolean addPeriod) {return Format(addPeriod, 55, amount);}
        public final String TriggerXTimes(Object orb, Integer times, boolean addPeriod)
        {
            return Format(addPeriod, 56, orb, times);
        }
        public final String Trigger(Object orb, Integer times, boolean addPeriod)
        {
            return Format(addPeriod, 57, orb, times);
        }
        public final String Use(Object target, boolean addPeriod)
        {
            return Format(addPeriod, 58, target);
        }

        private String Format(boolean addPeriod, int index, Object... objects)
        {
            String text = Strings.TEXT[index];
            return PCLJUtils.Format(text, objects) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        private String Format(boolean addPeriod, int index)
        {
            return Strings.TEXT[index] + (addPeriod ? LocalizedStrings.PERIOD : "");
        }
    }

    public static class Conditions
    {
        private final UIStrings Strings = GetUIStrings("Conditions");

        public final String Addition(Integer amount, Object cond, boolean addPeriod)
        {
            return Format(addPeriod, 0, amount, cond);
        }
        public final String IfMulti(Object desc1, Object desc2, boolean addPeriod)
        {
            return Format(addPeriod, 1, desc1, desc2);
        }
        public final String If(Object desc1, boolean addPeriod)
        {
            return Format(addPeriod, 2, desc1);
        }
        public final String NextTurn(boolean addPeriod)
        {
            return Format(addPeriod, 3);
        }
        public final String OnDiscard(boolean addPeriod)
        {
            return Format(addPeriod, 4);
        }
        public final String OnExhaust(boolean addPeriod)
        {
            return Format(addPeriod, 5);
        }
        public final String OnPurge(boolean addPeriod)
        {
            return Format(addPeriod, 6);
        }
        public final String OnGeneric(Object desc1, boolean addPeriod)
        {
            return Format(addPeriod, 7, desc1);
        }
        public final String WhenCreated(boolean addPeriod)
        {
            return Format(addPeriod, 8);
        }
        public final String WhenDrawn(boolean addPeriod)
        {
            return Format(addPeriod, 9);
        }
        public final String GenericMulti(Object desc1, Object desc2, boolean addPeriod)
        {
            return Format(addPeriod, 10, desc1, desc2);
        }
        public final String Generic(Object desc1, boolean addPeriod)
        {
            return Format(addPeriod, 11, desc1);
        }

        private String Format(boolean addPeriod, int index, Object... objects)
        {
            String text = Strings.TEXT[index];
            return PCLJUtils.Format(text, objects) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }
        private String Format(boolean addPeriod, int index)
        {
            return Strings.TEXT[index] + (addPeriod ? LocalizedStrings.PERIOD : "");
        }
    }

    public static class Trophies
    {
        private final UIStrings Strings = GetUIStrings("Trophies");

        public final String Bronze = Strings.TEXT[0];
        public final String Silver = Strings.TEXT[1];
        public final String Gold = Strings.TEXT[2];
        public final String BronzeDescription = Strings.TEXT[3];
        public final String SilverDescription = Strings.TEXT[4];
        public final String GoldDescription = Strings.TEXT[5];
        public final String BronzeLocked = Strings.TEXT[6];
        public final String SilverLocked = Strings.TEXT[7];
        public final String GoldLocked = Strings.TEXT[8];
        public final String GoldAccelWorld = Strings.TEXT[9];
        public final String PlatinumHint = Strings.TEXT[10];
        public final String PlatinumDescription = Strings.TEXT[11];
        public final String Platinum = Strings.TEXT[12];

        public final String BronzeFormatted(int ascension)
        {
            return PCLJUtils.Format(BronzeDescription, ascension);
        }

        public final String SilverFormatted(int ascension)
        {
            return PCLJUtils.Format(SilverDescription, ascension);
        }

        public final String GoldFormatted(int ascension)
        {
            return PCLJUtils.Format(GoldDescription, ascension);
        }
    }

    public static class Tutorial
    {
        private final UIStrings Strings = GetUIStrings("Tutorial");

        public final String AffinityInfo = Strings.TEXT[0];
        public final String AffinityTutorial1 = Strings.TEXT[1];

        public final String[] TutorialItems() {
            return Strings.TEXT;
        }
    }

    public static class EternalTutorial
    {
        private final UIStrings Strings = GetUIStrings("Tutorial2");

        public final String AffinityInfo = Strings.TEXT[0];
        public final String AffinityTutorial1 = Strings.TEXT[1];

        public final String[] TutorialItems() {
            return Strings.TEXT;
        }
    }

    public static class TheUnnamedReign
    {
        public final UIStrings Strings = GetUIStrings("TheUnnamedReign");
        public final String Name = Strings.TEXT[0];
    }

    public static class HandSelection
    {
        public final UIStrings Strings = GetUIStrings("HandSelection");
        public final String MoveToDrawPile = Strings.TEXT[0];
        public final String GenericBuff = Strings.TEXT[1];
        public final String Copy = Strings.TEXT[2];
        public final String Activate = Strings.TEXT[3];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Obtain = CardRewardScreen.TEXT[1];
        public final String Retain = RetainCardsAction.TEXT[0];
    }

    public static class GridSelection
    {
        public final UIStrings Strings = GetUIStrings("GridSelection");
        public final String DiscardUpTo_F1 = Strings.TEXT[0];
        public final String MoveToDrawPile_F1 = Strings.TEXT[1];
        public final String TransformInto_F1 = Strings.TEXT[2];
        public final String ChooseCards_F1 = Strings.TEXT[3];
        public final String Purge_F1 = Strings.TEXT[4];
        public final String Scry = Strings.TEXT[5];
        public final String Fetch_F1 = Strings.TEXT[6];
        public final String Pay_F1 = Strings.TEXT[7];
        public final String Give_F1 = Strings.TEXT[8];
        public final String CardsInPile_F1 = Strings.TEXT[9];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Cycle = GamblingChipAction.TEXT[1];
        public final String ChooseOneCard = CardRewardScreen.TEXT[1];

        public final String DiscardUpTo(int amount)
        {
            return PCLJUtils.Format(DiscardUpTo_F1, amount);
        }

        public final String MoveToDrawPile(int amount)
        {
            return PCLJUtils.Format(MoveToDrawPile_F1, amount);
        }

        public final String TransformInto(String name)
        {
            return PCLJUtils.Format(TransformInto_F1, name);
        }

        public final String ChooseCards(int amount)
        {
            return PCLJUtils.Format(ChooseCards_F1, amount);
        }

        public final String Purge(int amount)
        {
            return PCLJUtils.Format(Purge_F1, amount);
        }

        public final String Fetch(int amount)
        {
            return PCLJUtils.Format(Fetch_F1, amount);
        }

        public final String PayAffinity(int amount)
        {
            return PCLJUtils.Format(Pay_F1, amount);
        }

        public final String Give(int amount, Object item)
        {
            return PCLJUtils.Format(Give_F1, amount, item);
        }

        public final String CardsInPile(Object item, int amount)
        {
            return PCLJUtils.Format(CardsInPile_F1, item, amount);
        }
    }

    private static UIStrings GetUIStrings(String id)
    {
        return PGR.GetUIStrings(PGR.CreateID(PCLResources.ID, id));
    }
}