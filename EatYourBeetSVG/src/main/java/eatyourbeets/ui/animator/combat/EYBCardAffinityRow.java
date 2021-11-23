package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.effects.affinity.ChangeAffinityCountEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.*;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);
    public static final int SYNERGY_MULTIPLIER = 3;

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;

    public int MaxActivationsPerTurn;
    public int AvailableActivations;
    public int ActivationPowerAmount;
    public int Level;
    public int Total;
    protected float fontScale = 1.0F;

    public final GUI_Image image_background;
    public final GUI_Image image_affinity;
    public final GUI_Label current_affinity;
    public final GUI_Label total_affinity;
    public final GUI_Image image_synergy;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;
        final float offset_y = -0.5f -(index * 0.975f);

        Type = affinity;
        System = system;
        Power = system.GetPower(affinity);

        if (Power != null)
        {
            Power.hb = new RelativeHitbox(hb, 1, 1, 2.75f, offset_y);
        }

        image_background = new GUI_Image(GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        current_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 1f)
        .SetText("-");

        total_affinity = new GUI_Label(FontHelper.powerAmountFont,
                new RelativeHitbox(hb, 0.5f, 1f, 1.15f, offset_y))
                .SetAlignment(0.5f, 0f)
                .SetColor(Colors.Cream(0.6f))
                .SetText("");

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), 1.95f * hb.width, offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void ActivateSynergyBonus(AbstractCard card)
    {
        AvailableActivations -= 1;
        GameActions.Bottom.StackAffinityPower(Type, ActivationPowerAmount, false);
        CombatStats.OnSynergyBonus(card, Type);
    }

    public void OnStartOfTurn()
    {
        AvailableActivations = MaxActivationsPerTurn;

        if (Power != null)
        {
            Power.atStartOfTurn();
        }
    }

    public void Initialize()
    {
        AvailableActivations = MaxActivationsPerTurn = 1;
        ActivationPowerAmount = SYNERGY_MULTIPLIER;

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
        }
    }

    public void Flash() {
        this.fontScale = 8.0F;
        GameEffects.List.Add(new ChangeAffinityCountEffect(this, true));
    }

    public void Update(EYBCardAffinitySystem.AffinityCounts previewAffinities, EYBCard hoveredCard, EYBCardAffinities synergies, boolean draggingCard)
    {
        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = (AvailableActivations > 0) ? 1f : 0.25f;
        boolean isTriggering = false;

        if (Type == Affinity.General)
        {
            int am = previewAffinities.GetAmount(Affinity.General);
            Level = previewAffinities.GetAmount(Type, false) + am;
            if (Power != null) {
                Total = Power.experience + am;
            }

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                final int best = previewAffinities.GetAmount(Affinity.General);
                for (Affinity affinity : Affinity.Extended()) {
                    if (previewAffinities.GetAmount(affinity) == best) {
                        for (AbstractCard c : AbstractDungeon.player.hand.group)
                        {
                            final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                            if (temp == null || (temp.affinities.GetLevel(affinity) == 0 && temp.affinities.GetDirectLevel(Affinity.General) == 0))
                            {
                                c.transparency = 0.35f;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            Level = previewAffinities.GetAmount(Type, false);
            if (Power != null) {
                Total = Power.experience;
            }

            if (hoveredCard != null)
            {
                if ((hoveredCard.affinities.GetLevel(Type,false) > 0 || (hoveredCard.affinities.GetLevel(Affinity.Star,false) > 0 && System.GetLastAffinityLevel(Type) > 0)) && hoveredCard.cardData.CanGrantAffinity) {
                    Level += 1;
                    Total += 1;
                }
                final EYBCardAffinity a = (synergies != null && synergies.GetLevel(Affinity.Star) == 0) ? synergies.Get(Type) : null;
                if (System.CanActivateSynergyBonus(a))
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
                }
                else if (hoveredCard.affinities.GetLevel(Type, true) > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }


                if (hoveredCard.affinities.GetRequirement(Type) > 0 && hoveredCard.CheckAffinity(Type)) {
                    Level -= hoveredCard.affinities.GetRequirement(Type);
                    isTriggering = true;
                }
            }

            if (!draggingCard && image_background.hb.hovered && !System.hb.IsDragging())
            {
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                {
                    final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                    if (temp == null || (temp.affinities.GetLevel(Type) == 0))
                    {
                        c.transparency = 0.35f;
                    }
                }
            }
        }

        if (this.fontScale != 1.0F) {
            this.fontScale = MathUtils.lerp(this.fontScale, 1.0F, Gdx.graphics.getDeltaTime() * 10.0F);
            if (this.fontScale - 1.0F < 0.05F) {
                this.fontScale = 1.0F;
            }
        }

        current_affinity
                .SetText(Level)
                .SetColor(isTriggering ? Colors.Green(1) : Colors.Cream(Level > 0 ? 1 : 0.6f))
                .SetFontScale(this.fontScale);
        total_affinity
                .SetText(Power != null ? JUtils.Format("/{0}/{1}", Total, Power.GetCurrentThreshold()) : "")
                .SetFontScale(this.fontScale);

        Update();
    }

    @Override
    public void Update()
    {
        image_background.TryUpdate();
        image_affinity.TryUpdate();
        current_affinity.TryUpdate();
        total_affinity.TryUpdate();
        image_synergy.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        current_affinity.TryRender(sb);
        total_affinity.TryRender(sb);
        image_synergy.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }
    }
}