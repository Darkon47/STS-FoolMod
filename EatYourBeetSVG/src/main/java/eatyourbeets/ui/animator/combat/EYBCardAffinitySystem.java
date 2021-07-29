package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.powers.ApplyAffinityPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.*;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class EYBCardAffinitySystem extends GUIElement implements OnStartOfTurnSubscriber
{
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public final EYBCardAffinities BonusAffinities = new EYBCardAffinities(null);
    public final ForcePower Force;
    public final AgilityPower Agility;
    public final IntellectPower Intellect;
    public final WillpowerPower Willpower;
    public final BlessingPower Blessing;
    public final CorruptionPower Corruption;

    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final ArrayList<EYBCardAffinityRow> rows = new ArrayList<>();

    protected AbstractCard currentSynergy = null;
    protected AnimatorCard lastCardPlayed = null;

    public EYBCardAffinitySystem()
    {
        Powers.add(Force = new ForcePower());
        Powers.add(Agility = new AgilityPower());
        Powers.add(Intellect = new IntellectPower());
        Powers.add(Willpower = new WillpowerPower());
        Powers.add(Blessing = new BlessingPower());
        Powers.add(Corruption = new CorruptionPower());

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        final AffinityType[] types = AffinityType.BasicTypes();
        final int length = types.length;
        for (int i = 0; i < length; i++)
        {
            rows.add(new EYBCardAffinityRow(this, types[i], i, length + 1));
        }
        rows.add(new EYBCardAffinityRow(this, AffinityType.General, length, length + 1));
    }

    public EYBCardAffinities GetAffinities(Iterable<AbstractCard> cards, AbstractCard ignored)
    {
        EYBCardAffinities affinities = new EYBCardAffinities(null);
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
        return GetAffinities(player.hand.group, ignored).Add(BonusAffinities);
    }

    public int GetAffinityLevel(AffinityType type, AbstractCard ignored)
    {
        return GetHandAffinities(ignored).GetLevel(type, false);
    }

    public EYBCardAffinityRow GetRow(AffinityType type)
    {
        for (EYBCardAffinityRow row : rows)
        {
            if (row.Type == type)
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

    public AbstractAffinityPower GetPower(AffinityType type)
    {
        for (AbstractAffinityPower p : Powers)
        {
            if (p.affinityType.equals(type))
            {
                return p;
            }
        }

        return null;
    }

    public int GetPowerAmount(AffinityType type)
    {
        AbstractAffinityPower p = GetPower(type);
        return p == null ? 0 : p.amount;
    }

    @Override
    public void OnStartOfTurn()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.atStartOfTurn();
        }
    }

    public int GetLastAffinityLevel(AffinityType type)
    {
        return lastCardPlayed == null ? 0 : lastCardPlayed.affinities.GetLevel(type);
    }

    public AnimatorCard GetLastCardPlayed()
    {
        return lastCardPlayed;
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
        return affinity.level >= 2 && GetLastAffinityLevel(affinity.Type) > 0 && CanActivateSynergyBonus(affinity.Type);
    }

    public boolean CanActivateSynergyBonus(AffinityType type)
    {
        return type.ID >= 0 && CombatStats.CanActivateSemiLimited(type.name());
    }

    public ApplyAffinityPower ActivateSynergyBonus(AffinityType type)
    {
        return type.ID >= 0 ? GameActions.Bottom.StackAffinityPower(type, 1, false) : null;
    }

    public void OnSynergy(AnimatorCard card)
    {
        for (EYBCardAffinity affinity : card.affinities.List)
        {
            if (CanActivateSynergyBonus(affinity))
            {
                CombatStats.TryActivateSemiLimited(affinity.Type.name());
                ActivateSynergyBonus(affinity.Type);
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
        return GetSynergies(c1, c2).GetLevel(null) > 0;
    }

    public EYBCardAffinities GetSynergies(AbstractCard c1, AbstractCard c2)
    {
        final EYBCardAffinities synergies = new EYBCardAffinities(null);
        final EYBCard a = JUtils.SafeCast(c1, EYBCard.class);
        final EYBCard b = JUtils.SafeCast(c2, EYBCard.class);
        if (a == null || b == null)
        {
            return synergies;
        }

        for (AffinityType type : AffinityType.BasicTypes())
        {
            int lv_a = a.affinities.GetLevel(type);
            int lv_b = b.affinities.GetLevel(type);
            if ((lv_a > 1 && lv_b > 0) || (lv_b > 1 && lv_a > 0))
            {
                synergies.Add(type, lv_a);
            }
        }

        return synergies;
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK)
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

    public float ApplyScaling(AffinityType type, EYBCard card, float base)
    {
        if (type == AffinityType.Star)
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

        return ApplyScaling(GetPower(type), card, base);
    }

    public float ApplyScaling(AbstractAffinityPower power, EYBCard card, float base)
    {
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinityType, true) * power.amount * 0.5f);
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        for (AbstractAffinityPower p : Powers)
        {
            p.Initialize(player);
        }

        BonusAffinities.Star = null;
        BonusAffinities.List.clear();
        CombatStats.onStartOfTurn.Subscribe(this);
    }

    public void Update()
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
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

        final EYBCardAffinities handAffinities = GetHandAffinities(hoveredCard);
        final EYBCardAffinities synergies = GetSynergies(hoveredCard, lastCardPlayed);
        for (EYBCardAffinityRow row : rows)
        {
            row.Update(handAffinities, hoveredCard, synergies, draggingCard);
        }

        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }

        dragPanel_image.Update();
    }

    public void Render(SpriteBatch sb)
    {
        if (player == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        dragPanel_image.Render(sb);

        for (EYBCardAffinityRow t : rows)
        {
            t.Render(sb);
        }
    }
}