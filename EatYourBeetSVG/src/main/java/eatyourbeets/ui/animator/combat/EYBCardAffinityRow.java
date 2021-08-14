package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class EYBCardAffinityRow extends GUIElement
{
    public static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);
    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity Type;
    public final EYBCardAffinitySystem System;
    public final AbstractAffinityPower Power;
    public int MaxActivationsPerTurn;
    public int AvailableActivations;
    public int Level;

    protected final GUI_Image image_background;
    protected final GUI_Image image_affinity;
    protected final GUI_Label text_affinity;
    protected final GUI_Image image_synergy;

    public EYBCardAffinityRow(EYBCardAffinitySystem system, Affinity affinity, int index)
    {
        final Hitbox hb = system.hb;
        final float offset_y = -0.5f -(index * 0.975f);

        Type = affinity;
        System = system;
        Power = system.GetPower(affinity);

        if (Power != null)
        {
            Power.hb = new RelativeHitbox(hb, 1, 1, 1.5f, offset_y);
        }

        image_background = new GUI_Image(GR.Common.Images.Panel_Elliptical_Half_H.Texture(),
        new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
        .SetColor(COLOR_DEFAULT);

        image_affinity = new GUI_Image(affinity.GetIcon(),
        new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        text_affinity = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
        new RelativeHitbox(hb, 0.5f, 1f, 0.55f, offset_y))
        .SetAlignment(0.5f, 0.5f)
        .SetText("-");

        image_synergy = new GUI_Image(GR.Common.Images.Arrow_Right.Texture(),
        new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(18f), offset_y * hb.height, false));

        image_synergy.SetActive(Power != null);
    }

    public void ActivateSynergyBonus()
    {
        AvailableActivations -= 1;
        GameActions.Bottom.StackAffinityPower(Type, 1, false);
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

        if (Power != null)
        {
            Power.Initialize(AbstractDungeon.player);
        }
    }

    public void Update(EYBCardAffinities handAffinities, EYBCard hoveredCard, EYBCardAffinities synergies, boolean draggingCard)
    {
        image_background.SetColor(COLOR_DEFAULT);
        image_synergy.color.a = (AvailableActivations > 0) ? 1f : 0.25f;
        text_affinity.SetText(Level = handAffinities.GetLevel(Type, false)).SetColor(Colors.Cream(Level > 0 ? 1 : 0.6f));

        if (Type != Affinity.General)
        {
            if ((System.BonusAffinities.GetLevel(Type) > 0))
            {
                text_affinity.SetColor(Colors.Green(1));
            }

            if (hoveredCard != null)
            {
                EYBCardAffinity a = synergies != null ? synergies.Get(Type) : null;
                if (a != null && System.CanActivateSynergyBonus(a))
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_STRONG);
                }
                else if (hoveredCard.affinities.GetLevel(Type, true) > 0)
                {
                    image_background.SetColor(COLOR_HIGHLIGHT_WEAK);
                }
            }
        }

        if (!draggingCard && image_background.hb.hovered)
        {
            Affinity affinity = Type;
            if (affinity == Affinity.General)
            {
                final EYBCardAffinity best = handAffinities.Get(Affinity.General);
                affinity = best == null ? null : best.type;
            }

            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                final EYBCard temp = JUtils.SafeCast(c, EYBCard.class);
                if (temp == null || temp.affinities.GetLevel(affinity) == 0)
                {
                    c.transparency = 0.35f;
                }
            }
        }

        Update();
    }

    @Override
    public void Update()
    {
        image_background.TryUpdate();
        image_affinity.TryUpdate();
        text_affinity.TryUpdate();
        image_synergy.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_background.TryRender(sb);
        image_affinity.TryRender(sb);
        text_affinity.TryRender(sb);
        image_synergy.TryRender(sb);

        if (Power != null)
        {
            Power.Render(sb);
        }
    }
}