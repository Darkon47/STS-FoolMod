package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_ImageRegion;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

public class EYBCombatInfo_AffinityRow extends GUIElement
{
    public final AffinityType Type;

    protected static final Color COLOR_LV2_SYNERGY = new Color(0.75f, 0.75f, 0.35f, 0.75f);
    protected static final Color COLOR_CARD_AFFINITY = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    protected static final Color COLOR_DEFAULT = new Color(0.05f, 0.05f, 0.05f, 1f);

    protected final GUI_Image background_image;
    protected final GUI_Image affinity_image;
    protected final GUI_Label affinity_text;
    protected final GUI_ImageRegion synergy_icon;

    public EYBCombatInfo_AffinityRow(AffinityType type, Hitbox hb, int index, int max)
    {
        Type = type;

        float offset_y = -0.5f -(index * 0.975f);

        background_image = new GUI_Image(GR.Common.Images.Panel_Rounded_Half_H.Texture(),
                new RelativeHitbox(hb, 1, 1, 0.5f, offset_y))
                .SetColor(COLOR_DEFAULT);

        affinity_image = new GUI_Image(type.GetIcon(),
                new RelativeHitbox(hb, Scale(36), Scale(36), Scale(12f), offset_y * hb.height, false));

        affinity_text = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new RelativeHitbox(hb, 0.5f, 1f, 0.5f, offset_y))
                .SetAlignment(0.5f, 0.5f)
                .SetText("-");

        synergy_icon = new GUI_ImageRegion(type.GetSynergyEffectIcon(),
                new RelativeHitbox(hb, Scale(20), Scale(20), hb.width - Scale(10f), offset_y * hb.height, false));
    }

    public void Update(EYBCardAffinities handAffinities, EYBCardAffinities cardAffinities, EYBCardAffinities strongSynergies, boolean draggingCard)
    {
        boolean synergyEffectAvailable = !CombatStats.HasActivatedSemiLimited(Type.name());
        int total = handAffinities.GetLevel(Type, false);
        affinity_text.SetText(total);// > 0 ? total : "-");
        synergy_icon.color.a = synergyEffectAvailable ? 1f : 0.35f;

        int level = strongSynergies == null ? 0 : strongSynergies.GetLevel(Type, false);
        if (level == 2 && synergyEffectAvailable)
        {
            background_image.SetColor(COLOR_LV2_SYNERGY);
        }
        else
        {
            level = cardAffinities == null ? 0 : cardAffinities.GetLevel(Type, false);
            if (level == 0)
            {
                background_image.SetColor(COLOR_DEFAULT);
            }
            else
            {
                background_image.SetColor(COLOR_CARD_AFFINITY);
            }
        }

        if (!draggingCard && background_image.hb.hovered)
        {
            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                EYBCard t = JUtils.SafeCast(c, EYBCard.class);
                if (t == null || t.affinities.GetLevel(Type) == 0)
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
        background_image.Update();
        affinity_image.Update();
        affinity_text.Update();
        synergy_icon.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);
        affinity_image.Render(sb);
        affinity_text.Render(sb);
        synergy_icon.Render(sb);
    }
}