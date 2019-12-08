package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.orbs.Aether;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.common.TemporaryBiasPower;
import eatyourbeets.powers.unnamed.ResonancePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameUtilities
{
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();
    private static final Logger Logger = LogManager.getLogger(GameUtilities.class.getName());

    public static void ApplyTemporaryDexterity(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new LoseDexterityPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new DexterityPower(target, amount), amount);
        GameActionsHelper.ResetOrder();
    }

    public static void ApplyTemporaryFocus(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new TemporaryBiasPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new FocusPower(target, amount), amount);
        GameActionsHelper.ResetOrder();
    }

    public static void GainTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new LoseStrengthPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new StrengthPower(target, amount), amount);
        GameActionsHelper.ResetOrder();
    }

    public static int GetActualAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return AbstractDungeon.ascensionLevel;
        }

        return 0;
    }

    public static ArrayList<AbstractCreature> GetAllCharacters(boolean aliveOnly)
    {
        ArrayList<AbstractCreature> characters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                //logger.info("ENEMY: " + m.name + ", DeadOrEscaped: " + m.isDeadOrEscaped() + ", Dying: " + m.isDying);
                if (!aliveOnly || (!m.isDeadOrEscaped() && !m.isDying))
                {
                    characters.add(m);
                }
            }
        }

        characters.add(AbstractDungeon.player);

        return characters;
    }

    public static HashSet<AbstractCard> GetAllCopies(AbstractCard card)
    {
        String cardID = card.cardID;
        HashSet<AbstractCard> cards = new HashSet<>();
        AbstractCard c;

        c = AbstractDungeon.player.cardInUse;
        if (c != null && c.cardID.equals(cardID))
        {
            cards.add(c);
        }

        Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.discardPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.exhaustPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.limbo.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.hand.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInBattleInstances(AbstractCard card)
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances.get(card.uuid);

        cards.add(card);

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInstances(AbstractCard card)
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances(card);

        AbstractCard masterDeckInstance = GetMasterDeckInstance(card);
        if (masterDeckInstance != null)
        {
            cards.add(masterDeckInstance);
        }

        return cards;
    }

    public static int GetAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return Math.max(0, Math.min(20, AbstractDungeon.ascensionLevel));
        }

        return 0;
    }

    public static ArrayList<AbstractMonster> GetCurrentEnemies(boolean aliveOnly)
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                //logger.info("ENEMY: " + m.name + ", DeadOrEscaped: " + m.isDeadOrEscaped() + ", Dying: " + m.isDying);
                if (!aliveOnly || (!m.isDeadOrEscaped() && m.currentHealth >0))
                {
                    monsters.add(m);
                }
            }
        }

        return monsters;
    }

    public static AbstractRoom GetCurrentRoom()
    {
        MapRoomNode mapNode = AbstractDungeon.currMapNode;
        if (mapNode == null)
        {
            return null;
        }
        else
        {
            return mapNode.getRoom();
        }
    }

    public static int GetDebuffsCount(ArrayList<AbstractPower> powers)
    {
        int result = 0;

        for (AbstractPower power : powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                result += 1;
            }
        }

        return result;
    }

    public static int GetDexterity()
    {
        return GetDexterity(AbstractDungeon.player);
    }

    public static int GetDexterity(AbstractCreature creature)
    {
        DexterityPower power = (DexterityPower) creature.getPower(DexterityPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetFocus()
    {
        return GetFocus(AbstractDungeon.player);
    }

    public static int GetFocus(AbstractCreature creature)
    {
        FocusPower power = (FocusPower) creature.getPower(FocusPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static float GetHealthPercentage(AbstractCreature creature)
    {
        return creature.currentHealth / (float)creature.maxHealth;
    }

    public static Logger GetLogger(Class c)
    {
        return LogManager.getLogger(c.getName());
    }

    public static AbstractCard GetMasterDeckInstance(AbstractCard card)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid == card.uuid)
            {
                return c;
            }
        }

        return null;
    }

    public static HashSet<AbstractCard> GetOtherCardsInHand(AbstractCard card)
    {
        HashSet<AbstractCard> cards = new HashSet<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c != card)
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static <T> T GetPower(AbstractCreature owner, Class<T> powerType)
    {
        for (AbstractPower power : owner.powers)
        {
            if (powerType.isInstance(power))
            {
                return powerType.cast(power);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractPower> T GetPower(AbstractCreature creature, String powerID)
    {
        for (AbstractPower p : creature.powers)
        {
            if (p != null && powerID.equals(p.ID))
            {
                try
                {
                    return (T)p;
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();

                    return null;
                }
            }
        }

        return null;
    }

    public static int GetPowerAmount(String powerID)
    {
        return GetPowerAmount(AbstractDungeon.player, powerID);
    }

    public static int GetPowerAmount(AbstractCreature owner, String powerID)
    {
        AbstractPower power = GetPower(owner, powerID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static AbstractCreature GetRandomCharacter(boolean aliveOnly)
    {
        RandomizedList<AbstractMonster> enemies = new RandomizedList<>(GetCurrentEnemies(aliveOnly));

        AbstractCreature result = enemies.Retrieve(AbstractDungeon.cardRandomRng, false);
        if (result == null)
        {
            return AbstractDungeon.player;
        }
        else
        {
            return result;
        }
    }

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return JavaUtilities.GetRandomElement(GetCurrentEnemies(aliveOnly));
    }

    public static AbstractOrb GetRandomOrb()
    {
        if (orbs.Count() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost(), 7);
            orbs.Add(new Earth(), 6);
            orbs.Add(new Fire(), 6);
            orbs.Add(new Plasma(), 4);
            orbs.Add(new Dark(), 4);
            orbs.Add(new Aether(), 4);
        }

        return orbs.Retrieve(AbstractDungeon.cardRandomRng, false).makeCopy();
    }

    public static int GetResonance()
    {
        return GetResonance(AbstractDungeon.player);
    }

    public static int GetResonance(AbstractCreature creature)
    {
        ResonancePower power = (ResonancePower) creature.getPower(ResonancePower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetStrength()
    {
        return GetStrength(AbstractDungeon.player);
    }

    public static int GetStrength(AbstractCreature creature)
    {
        StrengthPower power = (StrengthPower) creature.getPower(StrengthPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetUniqueOrbsCount()
    {
        ArrayList<String> orbList = new ArrayList<>();

        for (AbstractOrb o : AbstractDungeon.player.orbs)
        {
            if (o.ID != null && !o.ID.equals(EmptyOrbSlot.ORB_ID) && !orbList.contains(o.ID))
            {
                orbList.add(o.ID);
            }
        }

        return orbList.size();
    }

    public static boolean InBattle()
    {
        AbstractRoom room = GetCurrentRoom();
        if (room != null && !room.isBattleOver)
        {
            return room.phase == AbstractRoom.RoomPhase.COMBAT || (room.monsters != null && !room.monsters.areMonstersBasicallyDead());
        }

        return false;
    }

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
    }

    public static boolean IsCurseOrStatus(AbstractCard card)
    {
        return card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS;
    }

    public static boolean IsDeadOrEscaped(AbstractCreature target)
    {
        return target == null || target.isDeadOrEscaped() || target.currentHealth <= 0;
    }

    public static void LoseTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPower(source, target, new StrengthPower(target, -amount), -amount);
            GameActionsHelper.ApplyPowerSilently(source, target, new GainStrengthPower(target, amount), amount);
        }

        GameActionsHelper.ResetOrder();
    }

    public static void UnlockAllKeys()
    {
        if (!Settings.isFinalActAvailable)
        {
            Settings.isFinalActAvailable = true;
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.IRONCLAD.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.THE_SILENT.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.DEFECT.name() + "_WIN", true);

            if (UnlockTracker.isAchievementUnlocked("RUBY_PLUS"))
            {
                UnlockTracker.unlockAchievement("RUBY_PLUS");
            }

            if (UnlockTracker.isAchievementUnlocked("EMERALD_PLUS"))
            {
                UnlockTracker.unlockAchievement("EMERALD_PLUS");
            }

            if (UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS"))
            {
                UnlockTracker.unlockAchievement("SAPPHIRE_PLUS");
            }
        }
    }

    public static boolean UseArtifact(AbstractCreature target)
    {
        ArtifactPower artifact = JavaUtilities.SafeCast(target.getPower(ArtifactPower.POWER_ID), ArtifactPower.class);
        if (artifact != null)
        {
            AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(target, ApplyPowerAction.TEXT[0]));
            CardCrawlGame.sound.play("NULLIFY_SFX");
            artifact.flashWithoutSound();
            artifact.onSpecificTrigger();

            return false;
        }
        else
        {
            return true;
        }
    }

    public static void UsePenNib()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
        }
    }
}
