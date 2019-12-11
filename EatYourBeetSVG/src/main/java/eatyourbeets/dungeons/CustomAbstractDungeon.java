package eatyourbeets.dungeons;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.ultrarare.*;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.utilities.JavaUtilities;
import patches.AbstractEnums;

import java.util.ArrayList;

public class CustomAbstractDungeon extends AbstractDungeon
{
    private static final MonsterRoom rollRarityRoom = new MonsterRoom();

    public CustomAbstractDungeon(String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList)
    {
        super(name, levelId, p, newSpecialOneTimeEventList);
    }

    public CustomAbstractDungeon(String name, AbstractPlayer p, SaveFile saveFile)
    {
        super(name, p, saveFile);
    }

    @Override
    protected void initializeLevelSpecificChances() { }
    @Override
    protected ArrayList<String> generateExclusions() { return null; }
    @Override
    protected void generateMonsters() { }
    @Override
    protected void generateWeakEnemies(int i) { }
    @Override
    protected void generateStrongEnemies(int i) { }
    @Override
    protected void generateElites(int i) { }
    @Override
    protected void initializeBoss() { }
    @Override
    protected void initializeEventList() { }
    @Override
    protected void initializeEventImg() { }
    @Override
    protected void initializeShrineList() { }


    public static ArrayList<AbstractCard> getRewardCards(Synergy synergy)
    {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;

        if (player.hasRelic("Question Card"))
        {
            ++numCards;
        }

        if (player.hasRelic("Busted Crown"))
        {
            numCards -= 2;
        }

        if (ModHelper.isModEnabled("Binary"))
        {
            --numCards;
        }

        ArrayList<AnimatorCard> animatorCards;

        if (synergy.equals(Synergies.ANY))
        {
            animatorCards = Synergies.GetColorlessCards();
        }
        else
        {
            animatorCards = Synergies.GetCardsWithSynergy(synergy);
        }

        ArrayList<AnimatorCard> common = new ArrayList<>();
        ArrayList<AnimatorCard> uncommon = new ArrayList<>();
        ArrayList<AnimatorCard> rare = new ArrayList<>();
        for (AnimatorCard card : animatorCards)
        {
            switch (card.rarity)
            {
                case COMMON:
                    common.add(card);
                    break;

                case UNCOMMON:
                    uncommon.add(card);
                    break;

                case RARE:
                    rare.add(card);
                    break;
            }
        }

        for(int i = 0; i < numCards; ++i)
        {
            AbstractCard.CardRarity rarity;

            if (synergy.equals(Synergies.ANY))
            {
                rarity = cardRng.randomBoolean(0.16f) ? AbstractCard.CardRarity.RARE : AbstractCard.CardRarity.UNCOMMON;
            }
            else
            {
                int roll = cardRng.random(99);
                roll += cardBlizzRandomizer;
                rarity = rollRarityRoom.getCardRarity(roll);

                switch(rarity)
                {
                    case COMMON:
                        cardBlizzRandomizer -= cardBlizzGrowth;
                        if (cardBlizzRandomizer <= cardBlizzMaxOffset)
                        {
                            cardBlizzRandomizer = cardBlizzMaxOffset;
                        }
                    case UNCOMMON:
                        break;

                    case RARE:
                        cardBlizzRandomizer = cardBlizzStartOffset;
                        break;

                    default:
                        logger.info("I don't know");
                }
            }

            if (rarity == AbstractCard.CardRarity.COMMON && common.size() == 0)
            {
                if (uncommon.size() > 0)
                {
                    rarity = AbstractCard.CardRarity.UNCOMMON;
                }
                else
                {
                    rarity = AbstractCard.CardRarity.RARE;
                }
            }
            else if (rarity == AbstractCard.CardRarity.UNCOMMON && uncommon.size() == 0)
            {
                if (common.size() > 0)
                {
                    rarity = AbstractCard.CardRarity.COMMON;
                }
                else
                {
                    rarity = AbstractCard.CardRarity.RARE;
                }
            }
            else if (rarity == AbstractCard.CardRarity.RARE && rare.size() == 0)
            {
                if (uncommon.size() > 0)
                {
                    rarity = AbstractCard.CardRarity.UNCOMMON;
                }
                else
                {
                    rarity = AbstractCard.CardRarity.COMMON;
                }
            }

            AbstractCard c = null;
            switch (rarity)
            {
                case COMMON:
                    c = JavaUtilities.GetRandomElement(common, cardRng);
                    common.remove(c);
                    break;

                case UNCOMMON:
                    c = JavaUtilities.GetRandomElement(uncommon, cardRng);
                    uncommon.remove(c);
                    break;

                case RARE:
                    c = JavaUtilities.GetRandomElement(rare, cardRng);
                    rare.remove(c);
                    break;
            }

            if (c != null)
            {
                AbstractCard copy = c.makeCopy();
                if (copy.rarity != AbstractCard.CardRarity.RARE && cardRng.randomBoolean(cardUpgradedChance) && copy.canUpgrade())
                {
                    copy.upgrade();
                }
                else if (copy.type == AbstractCard.CardType.ATTACK && player.hasRelic(MoltenEgg2.ID))
                {
                    copy.upgrade();
                }
                else if (copy.type == AbstractCard.CardType.SKILL && player.hasRelic(ToxicEgg2.ID))
                {
                    copy.upgrade();
                }
                else if (copy.type == AbstractCard.CardType.POWER && player.hasRelic(FrozenEgg2.ID))
                {
                    copy.upgrade();
                }

                retVal.add(copy);
            }
        }

        AddUltraRare(retVal, synergy);

        return retVal;
    }

    public static float GetUltraRareChance()
    {
        float bonus = 1;
        int level = AnimatorCustomLoadout.specialTrophies.trophy1;
        if (level > 0)
        {
            bonus += level / (level + 100f);
        }

        return 4f * bonus;
    }

    private static void AddUltraRare(ArrayList<AbstractCard> cards, Synergy synergy)
    {
        int currentLevel = UnlockTracker.getUnlockLevel(AbstractEnums.Characters.THE_ANIMATOR);
        if (currentLevel <= 2 || AbstractDungeon.floorNum < 8 || AbstractDungeon.floorNum > 36)
        {
            return;
        }

        float chances = GetUltraRareChance();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c instanceof AnimatorCard_UltraRare)
            {
                if (synergy.ID == ((AnimatorCard_UltraRare) c).GetSynergy().ID)
                {
                    return; // No duplicates
                }
                else
                {
                    chances *= 0.5f;
                }
            }
        }

        float roll = AbstractDungeon.cardRng.random(100f);
        if (roll > chances)
        {
            return;
        }

        AbstractCard card = null;

        if (synergy == Synergies.Konosuba)
        {
            card = new Chomusuke();
        }
        else if (synergy == Synergies.Gate)
        {
            card = new Giselle();
        }
        else if (synergy == Synergies.Elsword)
        {
            card = new Rose();
        }
        else if (synergy == Synergies.Overlord)
        {
            card = new SirTouchMe();
        }
        else if (synergy == Synergies.Katanagatari)
        {
            card = new ShikizakiKiki();
        }
        else if (synergy == Synergies.OwariNoSeraph)
        {
            card = new HiiragiTenri();
        }
        else if (synergy == Synergies.FullmetalAlchemist)
        {
            card = new Truth();
        }
        else if (synergy == Synergies.GoblinSlayer)
        {
            card = new Hero();
        }
        else if (synergy == Synergies.Chaika)
        {
            card = new NivaLada();
        }
        else if (synergy == Synergies.TenSura)
        {
            card = new Veldora();
        }
        else if (synergy == Synergies.NoGameNoLife)
        {
            card = new Azriel();
        }
        else if (synergy == Synergies.Fate)
        {
            card = new JeanneDArc();
        }
        else if (synergy == Synergies.OnePunchMan)
        {
            card = new SeriousSaitama();
        }
//        else if (synergy == Synergies.ANY)
//        {
//            card = new InfinitePower();
//        }

        if (card != null && cards.size() > 0)
        {
            //card.tags.remove(AbstractEnums.CardTags.UNOBTAINABLE);
            cards.remove(0);
            cards.add(card);
        }
    }
}
