package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.FtueTip;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.*;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Ftue;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;
import java.util.Arrays;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class EYBCardAffinitySystem extends GUIElement implements OnStartOfTurnSubscriber
{
    public static final int SCALING_DIVISION = 1;
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public AffinityCounts AffinityCounts = new AffinityCounts();

    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Button info_icon;
    protected final ArrayList<EYBCardAffinityRow> rows = new ArrayList<>();
    protected EYBCardTooltip tooltip;
    protected Vector2 savedPosition;

    protected AbstractCard currentSynergy = null;
    protected AnimatorCard lastCardPlayed = null;

    public EYBCardAffinitySystem()
    {
        Powers.add(new MightPower());
        Powers.add(new VelocityPower());
        Powers.add(new WisdomPower());
        Powers.add(new EndurancePower());
        Powers.add(new SuperchargePower());
        Powers.add(new DesecrationPower());
        Powers.add(new TechnicPower());

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
        .SetColor(Colors.White(0.75f));

        tooltip = new EYBCardTooltip(GR.Tooltips.Affinity_General.title, GR.Animator.Strings.Tutorial.AffinityInfo);
        info_icon = new GUI_Button(ImageMaster.INTENT_UNKNOWN, new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(100f), Scale(20f), false))
                .SetText("")
                .SetOnClick(() ->
                {AbstractDungeon.ftue = new GUI_Ftue(GR.Tooltips.Affinity_General.title, GR.Animator.Strings.Tutorial.AffinityTutorial1,
                        Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, FtueTip.TipType.NO_FTUE);})
        ;

        final Affinity[] types = Affinity.All();
        for (int i = 0; i < types.length; i++)
        {
            rows.add(new EYBCardAffinityRow(this, types[i], i));
        }

        //rows.add(new EYBCardAffinityRow(this, Affinity.General, types.length));
    }

    public AffinityCounts AddAffinity(Affinity affinity, int amount)
    {
        return AffinityCounts.Add(affinity, amount);
    }

    public AffinityCounts AddAffinities(EYBCardAffinities affinities)
    {
        return AffinityCounts.Add(affinities, 1);
    }

    public boolean CheckAffinityLevels(Affinity[] affinities, int amount, boolean addStar) {
        return CheckAffinityLevels(affinities, amount, addStar, false);
    }

    public boolean CheckAffinityLevels(Affinity[] affinities, int amount, boolean addStar, boolean requireAll) {
        for (Affinity affinity : affinities) {
            if (GetAffinityLevel(affinity, addStar) >= amount) {
                return true;
            }
            else if (requireAll) {
                return false;
            }
        }
        return requireAll;
    }

    public void Flash(Affinity affinity) {
        EYBCardAffinityRow row = GetRow(affinity);
        if (row != null) {
            row.Flash();
        }
    }

    public int GetAffinityLevel(Affinity affinity, boolean addStar) {
        int base = AffinityCounts.GetAmount(affinity, addStar);
        return CombatStats.OnTrySpendAffinity(affinity, base, addStar, false);
    }

    public EYBCardAffinities GetCardAffinities(Iterable<AbstractCard> cards, AbstractCard ignored)
    {
        final EYBCardAffinities affinities = new EYBCardAffinities(null);
        for (AbstractCard c : cards)
        {
            EYBCard card = JUtils.SafeCast(c, EYBCard.class);
            if (card != ignored && card != null)
            {
                affinities.Add(card.affinities, 1);
            }
        }

        return affinities;
    }

    public EYBCardAffinities GetHandAffinities(AbstractCard ignored)
    {
        return player == null ? new EYBCardAffinities(null) : GetCardAffinities(player.hand.group, ignored);
    }

    public int GetHandAffinityLevel(Affinity affinity, AbstractCard ignored)
    {
        return GetHandAffinities(ignored).GetLevel(affinity, false);
    }

    public EYBCardAffinityRow GetRow(Affinity affinity)
    {
        for (EYBCardAffinityRow row : rows)
        {
            if (row.Type == affinity)
            {
                return row;
            }
        }

        return null;
    }

    public boolean IsSynergizing(AbstractCard card)
    {
        return card != null && currentSynergy != null && currentSynergy.uuid == card.uuid;
    }

    public AbstractAffinityPower GetPower(Affinity affinity)
    {
        for (AbstractAffinityPower p : Powers)
        {
            if (p.affinity.equals(affinity))
            {
                return p;
            }
        }

        return null;
    }

    public int GetPowerAmount(Affinity affinity)
    {
        final AbstractAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.amount;
    }

    public int GetPowerLevel(Affinity affinity)
    {
        final AbstractAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.GetCurrentThreshold();
    }

    @Override
    public void OnStartOfTurn()
    {
        for (EYBCardAffinityRow row : rows)
        {
            row.OnStartOfTurn();
        }
    }

    public int GetLastAffinityLevel(Affinity affinity)
    {
        return lastCardPlayed == null ? 0 : lastCardPlayed.affinities.GetLevel(affinity);
    }

    public AnimatorCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public AffinityCounts SpendAffinity(Affinity affinity, int amount)
    {
        return AffinityCounts.Spend(affinity, amount);
    }

    public boolean TrySynergize(AbstractCard card)
    {
        if (WouldSynergize(card))
        {
            currentSynergy = card;
            return true;
        }

        currentSynergy = null;
        return false;
    }

    public boolean CanActivateSynergyBonus(EYBCardAffinity affinity)
    {
        return affinity != null && affinity.level + GetLastAffinityLevel(affinity.type) >= 3 && CanActivateSynergyBonus(affinity.type);
    }

    public boolean CanActivateSynergyBonus(Affinity affinity)
    {
        return affinity.ID >= 0 && GetRow(affinity).AvailableActivations > 0;
    }

    public void AddMaxActivationsPerTurn(Affinity affinity, int amount)
    {
        final EYBCardAffinityRow row = GetRow(affinity);
        row.MaxActivationsPerTurn = Math.max(0, row.MaxActivationsPerTurn + amount);
        row.AvailableActivations = Math.max(0, row.AvailableActivations + amount);
    }

    public void OnSynergy(AnimatorCard card)
    {
        for (EYBCardAffinity affinity : card.affinities.List)
        {
            if (CanActivateSynergyBonus(affinity))
            {
                final EYBCardAffinityRow row = GetRow(affinity.type);
                if (row != null)
                {
                    row.ActivateSynergyBonus(card);
                }
            }
        }
    }

    public void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        currentSynergy = null;
    }

    public boolean WouldSynergize(AbstractCard card)
    {
        return WouldSynergize(card, lastCardPlayed);
    }

    public boolean WouldSynergize(AbstractCard card, AbstractCard other)
    {
        for (OnSynergyCheckSubscriber s : CombatStats.onSynergyCheck.GetSubscribers())
        {
            if (s.OnSynergyCheck(card, other))
            {
                return true;
            }
        }

        if (card == null || other == null)
        {
            return false;
        }

        final AnimatorCard a = JUtils.SafeCast(card, AnimatorCard.class);
        final AnimatorCard b = JUtils.SafeCast(other, AnimatorCard.class);
        if (a != null)
        {
            return b != null ? (a.HasDirectSynergy(b) || b.HasDirectSynergy(a)) : a.HasDirectSynergy(other);
        }
        else
        {
            return b != null ? b.HasDirectSynergy(card) : HasDirectSynergy(card, other);
        }
    }

    public boolean HasDirectSynergy(AbstractCard c1, AbstractCard c2)
    {
        return GetSynergies(c1, c2).GetLevel(Affinity.General, false) > 0;
    }

    public EYBCardAffinities GetSynergies(AbstractCard current, AbstractCard previous)
    {
        final EYBCardAffinities synergies = new EYBCardAffinities(null);
        final EYBCard a = JUtils.SafeCast(current, EYBCard.class);
        final EYBCard b = JUtils.SafeCast(previous, EYBCard.class);
        if (a == null || b == null)
        {
            return synergies;
        }

        for (Affinity affinity : Affinity.Extended())
        {
            int lv_a = a.affinities.GetLevel(affinity);
            int lv_b = b.affinities.GetLevel(affinity);
            if ((lv_a > 1 && lv_b > 0) || (lv_b > 1 && lv_a > 0))
            {
                synergies.Add(affinity, lv_a);
            }
        }

        synergies.SetStar(a.affinities.GetLevel(Affinity.Star));

        return synergies;
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK || card.cardData.BlockScalingAttack)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    block = ApplyScaling(p, card, block);
                }
            }
        }

        return block;
    }

    public float ModifyDamage(float damage, EYBCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    damage = ApplyScaling(p, card, damage);
                }
            }
        }

        return damage;
    }

    public float ModifyMagicNumber(float magicNumber, EYBCard card) {
        if (card.cardData.CanScaleMagicNumber) {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    magicNumber = ApplyScaling(p, card, magicNumber);
                }
            }
        }
        return magicNumber;
    }

    public float ApplyScaling(Affinity affinity, EYBCard card, float base)
    {
        if (affinity == Affinity.Star)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    base = ApplyScaling(p, card, base);
                }
            }

            return base;
        }

        return ApplyScaling(GetPower(affinity), card, base);
    }

    public float ApplyScaling(AbstractAffinityPower power, EYBCard card, float base)
    {
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinity, true) * power.GetEffectiveScaling());
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        AffinityCounts = new AffinityCounts();
        CombatStats.onStartOfTurn.Subscribe(this);

        if (savedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) dragPanel_image.hb;
            savedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            savedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (savedPosition.dst2(GR.Animator.Config.AffinitySystemPosition.Get()) > Mathf.Epsilon)
            {
                JUtils.LogInfo(this, "Saved affinity panel position.");
                GR.Animator.Config.AffinitySystemPosition.Set(savedPosition.cpy(), true);
            }
        }

        for (EYBCardAffinityRow row : rows)
        {
            row.Initialize();
        }

        for (AbstractAffinityPower po : Powers) {
            CombatStats.onGainAffinity.Subscribe(po);
            CombatStats.onApplyPower.Subscribe(po);
            JUtils.LogError(this, po.name);
        }
    }

    public void Update()
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        if (savedPosition == null)
        {
            savedPosition = GR.Animator.Config.AffinitySystemPosition.Get(new Vector2(0.0522f, 0.43f)).cpy();
            hb.SetPosition(ScreenW(savedPosition.x), ScreenH(savedPosition.y));
        }

        boolean draggingCard = false;
        EYBCard hoveredCard = null;
        if (player.hoveredCard != null)
        {
            if ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode)
            {
                draggingCard = true;
            }
            if (player.hoveredCard instanceof EYBCard)
            {
                hoveredCard = (EYBCard)player.hoveredCard;
            }
        }

        final AffinityCounts previewAffinities = new AffinityCounts(AffinityCounts);
        final EYBCardAffinities synergies = GetSynergies(hoveredCard, lastCardPlayed);
        for (EYBCardAffinityRow row : rows)
        {
            row.Update(previewAffinities, hoveredCard, synergies, draggingCard);
        }

        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }

        dragPanel_image.Update();
        draggable_icon.Update();
        info_icon.Update();

        if (!draggingCard && info_icon.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip);
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        dragPanel_image.Render(sb);
        draggable_icon.Render(sb);
        info_icon.Render(sb);

        for (EYBCardAffinityRow t : rows)
        {
            t.Render(sb);
        }
    }

    public static class AffinityCounts {
        public int[] Counts = new int[Affinity.Extended().length];
        public int Star;

        public AffinityCounts() {
        }

        public AffinityCounts(AffinityCounts counts) {
            Counts = counts.Counts.clone();
        }

        public AffinityCounts(EYBCardAffinities affinities) {
            for (EYBCardAffinity affinity : affinities.List) {
                Counts[affinity.type.ID] = affinity.level;
            }
            if (affinities.Star != null) {
                Star = affinities.Star.level;
            }
        }

        public AffinityCounts Add(Affinity affinity, int amount) {
            int actualAmount = CombatStats.OnGainAffinity(affinity, amount, true);
            if (Affinity.Star.equals(affinity) || Affinity.General.equals(affinity)) {
                Star += actualAmount;
            }
            else {
                Counts[affinity.ID] += actualAmount;
            }
            return this;
        }

        public AffinityCounts Add(AffinityCounts counts) {
            for (int i = 0; i < Counts.length; i++) {
                Counts[i] += CombatStats.OnGainAffinity(Affinity.Extended()[i], counts.Counts[i], true);
            }
            Star += counts.Star;
            return this;
        }

        public AffinityCounts Add(EYBCardAffinities affinities) {
            for (EYBCardAffinity affinity : affinities.List) {
                Counts[affinity.type.ID] += CombatStats.OnGainAffinity(affinity.type, affinity.level, true);
            }
            if (affinities.Star != null) {
                Star += affinities.Star.level;
            }
            return this;
        }

        public AffinityCounts Add(EYBCardAffinities affinities, int amount) {
            for (EYBCardAffinity affinity : affinities.List) {
                int actualAmount = CombatStats.OnGainAffinity(affinity.type, amount, true);
                Counts[affinity.type.ID] += actualAmount;
            }
            if (affinities.Star != null) {
                Star += amount;
            }
            return this;
        }

        public AffinityCounts Spend(Affinity affinity, int amount) {
            if (Affinity.Star.equals(affinity) || Affinity.General.equals(affinity)) {
                Star -= amount;
                if (Star < 0) {
                    Star = 0;
                }
            }
            else {
                Counts[affinity.ID] -= amount;
                if (Counts[affinity.ID] < 0) {
                    Counts[affinity.ID] = 0;
                }
            }
            return this;
        }

        public int GetAmount(Affinity affinity)
        {
            return GetAmount(affinity,true);
        }

        public int GetAmount(Affinity affinity, boolean addStar)
        {
            if (affinity == Affinity.Star)
            {
                return Star;
            }
            else if (affinity == Affinity.General)
            {
                return Arrays.stream(Counts).max().getAsInt() + (addStar ? Star : 0);
            }
            else
            {
                return Counts[affinity.ID] + (addStar ? Star : 0);
            }
        }
    }
}