package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.ui.GUIElement;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class PCLAffinitySystem extends GUIElement {
    public static final int SCALING_DIVISION = 1;
    public final ArrayList<AbstractPCLAffinityPower> Powers = new ArrayList<>();
    public final FoolAffinityMeter AffinityMeter;
    public final FoolAffinityRows AffinityRows;
    public final EternalResolveMeter ResolveMeter;
    public PCLAffinityCounts AffinityCounts;

    protected PCLAffinity currentAffinitySynergy = null;
    protected AbstractCard currentSynergy = null;
    protected PCLCard lastCardPlayed = null;

    public PCLAffinitySystem() {
        for (PCLAffinity affinity : PCLAffinity.Extended()) {
            Powers.add(affinity.GetPower());
        }
        AffinityRows = new FoolAffinityRows(this);

        AffinityCounts = new PCLAffinityCounts();
        AffinityMeter = new FoolAffinityMeter();
        ResolveMeter = new EternalResolveMeter();
    }

    public PCLAffinityCounts AddAffinity(PCLAffinity affinity, int amount) {
        return AffinityCounts.Add(affinity, amount);
    }

    public PCLAffinityCounts AddAffinities(PCLCardAffinities affinities) {
        return AffinityCounts.Add(affinities);
    }

    public boolean CheckAffinityLevels(PCLAffinity[] affinities, int amount, boolean addStar) {
        return CheckAffinityLevels(affinities, amount, addStar, false);
    }

    public boolean CheckAffinityLevels(PCLAffinity[] affinities, int amount, boolean addStar, boolean requireAll) {
        for (PCLAffinity affinity : affinities) {
            if (GetAffinityLevel(affinity, addStar) >= amount) {
                return true;
            } else if (requireAll) {
                return false;
            }
        }
        return requireAll;
    }

    public int GetAffinityLevel(PCLAffinity affinity, boolean addStar) {
        int base = AffinityCounts.GetAmount(affinity);
        return PCLCombatStats.OnTrySpendAffinity(affinity, base, false);
    }

    public PCLCardAffinities GetCardAffinities(Iterable<AbstractCard> cards, AbstractCard ignored) {
        final PCLCardAffinities affinities = new PCLCardAffinities(null);
        for (AbstractCard c : cards) {
            PCLCard card = PCLJUtils.SafeCast(c, PCLCard.class);
            if (card != ignored && card != null) {
                affinities.Add(card.affinities, 1);
            }
        }

        return affinities;
    }

    public PCLCardAffinities GetHandAffinities(AbstractCard ignored) {
        return player == null ? new PCLCardAffinities(null) : GetCardAffinities(player.hand.group, ignored);
    }

    public int GetHandAffinityLevel(PCLAffinity affinity, AbstractCard ignored) {
        return GetHandAffinities(ignored).GetLevel(affinity, false);
    }

    public PCLAffinity GetLastAffinitySynergy() {
        return currentSynergy != null ? currentAffinitySynergy : null;
    }

    public boolean IsSynergizing(AbstractCard card) {
        return card != null && currentSynergy != null && currentSynergy.uuid == card.uuid;
    }

    public AbstractPCLAffinityPower GetPower(PCLAffinity affinity) {
        return affinity.ID < Powers.size() && affinity.ID >= 0 ? Powers.get(affinity.ID) : null;
    }

    public int GetPowerAmount(PCLAffinity affinity) {
        final AbstractPCLAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.amount;
    }

    public int GetPowerLevel(PCLAffinity affinity) {
        final AbstractPCLAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.GetEffectiveLevel();
    }

    public void OnStartOfTurn() {
        AffinityRows.OnStartOfTurn();
        GetActiveMeter().OnStartOfTurn();
    }

    public int GetLastAffinityLevel(PCLAffinity affinity) {
        return lastCardPlayed == null ? 0 : lastCardPlayed.affinities.GetLevel(affinity);
    }

    public PCLCard GetLastCardPlayed() {
        return lastCardPlayed;
    }

    public PCLAffinityCounts SpendAffinity(PCLAffinity affinity, int amount) {
        return AffinityCounts.Spend(affinity, amount);
    }

    public boolean TrySynergize(AbstractCard card) {
        if (WouldMatch(card)) {
            currentSynergy = card;
            currentAffinitySynergy = GetActiveMeter().GetCurrentAffinity();
            return true;
        }

        currentSynergy = null;
        currentAffinitySynergy = null;
        return false;
    }

    public void OnNotMatch(PCLCard card) {
        GetActiveMeter().OnNotMatch(card);
    }

    public void OnMatch(PCLCard card) {
        GetActiveMeter().OnMatch(card);
        AffinityRows.OnMatch(card);
    }

    public void SetLastCardPlayed(AbstractCard card) {
        lastCardPlayed = PCLJUtils.SafeCast(card, PCLCard.class);
        currentSynergy = null;
        currentAffinitySynergy = null;
    }

    //TODO Use this
    public boolean CanViewAffinityRows() {
        return PCLGameUtilities.IsPlayerClass(PGR.Enums.Characters.THE_FOOL);
    }

    public boolean WouldMatch(AbstractCard card) {
        for (OnSynergyCheckSubscriber s : PCLCombatStats.onSynergyCheck.GetSubscribers()) {
            if (s.OnSynergyCheck(card, null)) {
                return true;
            }
        }

        final PCLCard a = PCLJUtils.SafeCast(card, PCLCard.class);
        if (a != null) {
            return GetActiveMeter().HasMatch(a);
        }
        return false;
    }

    public boolean WouldSynergize(AbstractCard card, AbstractCard other) {
        for (OnSynergyCheckSubscriber s : PCLCombatStats.onSynergyCheck.GetSubscribers()) {
            if (s.OnSynergyCheck(card, other)) {
                return true;
            }
        }

        if (card == null || other == null) {
            return false;
        }

        final PCLCard a = PCLJUtils.SafeCast(card, PCLCard.class);
        final PCLCard b = PCLJUtils.SafeCast(other, PCLCard.class);
        if (a != null) {
            return b != null ? (a.HasDirectSynergy(b) || b.HasDirectSynergy(a)) : a.HasDirectSynergy(other);
        } else {
            return b != null ? b.HasDirectSynergy(card) : HasDirectSynergy(card, other);
        }
    }

    public boolean HasDirectSynergy(AbstractCard c1, AbstractCard c2) {
        return GetSynergies(c1, c2).GetLevel(PCLAffinity.General, false) > 0;
    }

    public PCLCardAffinities GetSynergies(AbstractCard current, AbstractCard previous) {
        final PCLCardAffinities synergies = new PCLCardAffinities(null);
        final PCLCard a = PCLJUtils.SafeCast(current, PCLCard.class);
        final PCLCard b = PCLJUtils.SafeCast(previous, PCLCard.class);
        if (a == null || b == null) {
            return synergies;
        }

        for (PCLAffinity affinity : PCLAffinity.Extended()) {
            int lv_a = a.affinities.GetLevel(affinity);
            int lv_b = b.affinities.GetLevel(affinity);
            if (lv_a > 0 && lv_b > 0) {
                synergies.Add(affinity, Math.min(lv_a, lv_b));
            }
        }

        int star_a = a.affinities.GetLevel(PCLAffinity.Star);
        int star_b = b.affinities.GetLevel(PCLAffinity.Star);
        if (star_a > 0 && star_b > 0) {
            synergies.SetStar(Math.min(star_a, star_b));
        } else {
            synergies.SetStar(Math.max(star_a, star_b));
        }

        return synergies;
    }

    public float ModifyBlock(float block, PCLCard card) {
        if (card.type != AbstractCard.CardType.ATTACK || card.cardData.BlockScalingAttack) {
            for (AbstractPCLAffinityPower p : Powers) {
                block = ApplyScaling(p, card, block);
            }
        }

        return block;
    }

    public float ModifyDamage(float damage, PCLCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            for (AbstractPCLAffinityPower p : Powers) {
                damage = ApplyScaling(p, card, damage);
            }
        }

        return damage;
    }

    public float ModifyMagicNumber(float magicNumber, PCLCard card) {
        if (card.cardData.CanScaleMagicNumber) {
            for (AbstractPCLAffinityPower p : Powers) {
                magicNumber = ApplyScaling(p, card, magicNumber);
            }
        }
        return magicNumber;
    }

    public float ApplyScaling(PCLAffinity affinity, PCLCard card, float base) {
        if (affinity == PCLAffinity.Star) {
            for (AbstractPCLAffinityPower p : Powers) {
                base = ApplyScaling(p, card, base);
            }

            return base;
        }

        return ApplyScaling(GetPower(affinity), card, base);
    }

    public float ApplyScaling(AbstractPCLAffinityPower power, PCLCard card, float base) {
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinity, true) * power.GetEffectiveScaling());
    }

    public PCLAffinityMeter GetActiveMeter() {
        if (PCLGameUtilities.IsPlayerClass(PGR.Enums.Characters.THE_ETERNAL)) {
            return ResolveMeter;
        }
        return AffinityMeter;
    }

    public void Initialize() {
        AffinityCounts = new PCLAffinityCounts();

        PCLJUtils.LogInfo(this, "Initialized PCL Affinity System.");

        AffinityMeter.Initialize();
        AffinityRows.Initialize();
        ResolveMeter.Initialize();
    }

    public void Update() {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden) {
            return;
        }

        boolean draggingCard = false;
        PCLCard hoveredCard = null;
        if (player.hoveredCard != null) {
            if ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode) {
                draggingCard = true;
            }
            if (player.hoveredCard instanceof PCLCard) {
                hoveredCard = (PCLCard) player.hoveredCard;
            }
        }

        GetActiveMeter().Update(hoveredCard);
        if (CanViewAffinityRows()) {
            AffinityRows.Update(hoveredCard);
        }

        for (int i = 0; i < Powers.size(); i++) {
            Powers.get(i).update(i);
        }
    }

    public void Render(SpriteBatch sb) {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden) {
            return;
        }

        GetActiveMeter().Render(sb);
        if (CanViewAffinityRows()) {
            AffinityRows.Render(sb);
        }
    }
}