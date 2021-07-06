package eatyourbeets.cards.base.attributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.RenderHelpers;

import java.util.HashMap;

public abstract class AbstractAttribute
{
    protected final static HashMap<AbstractCard.CardRarity, ColoredTexture> leftPanels = new HashMap<>();
    protected final static HashMap<AbstractCard.CardRarity, ColoredTexture> rightPanels = new HashMap<>();
    protected final static CommonImages.CardIcons ICONS = GR.Common.Images.Icons;
    protected final static float DESC_OFFSET_X = (AbstractCard.IMG_WIDTH * 0.5f);
    protected final static float DESC_OFFSET_Y = (AbstractCard.IMG_HEIGHT * 0.10f);
    protected static final GlyphLayout layout = new GlyphLayout();

    public static boolean leftAlign;

    public Texture icon;
    public ColoredString mainText;
    public String iconTag;
    public String suffix;

    public abstract AbstractAttribute SetCard(EYBCard card);

    public AbstractAttribute SetIconTag(String iconTag)
    {
        this.iconTag = iconTag;

        return this;
    }

    public AbstractAttribute SetIcon(Texture icon)
    {
        this.icon = icon;

        return this;
    }

    public AbstractAttribute SetText(ColoredString string)
    {
        this.mainText = string;

        return this;
    }

    public AbstractAttribute SetText(String text, Color color)
    {
        this.mainText = new ColoredString(text, color);

        return this;
    }

    public AbstractAttribute AddMultiplier(int times)
    {
        this.suffix = leftAlign ? ("x" + times) : (times + "x");

        return this;
    }

    public AbstractAttribute AddSuffix(String suffix)
    {
        this.suffix = suffix;

        return this;
    }

    public AbstractAttribute Clear()
    {
        this.suffix = null;
        this.iconTag = null;
        this.icon = null;
        this.mainText = null;

        return this;
    }

    public void Render(SpriteBatch sb, EYBCard card)
    {
        final float suffix_scale = 0.66f;
        final float cw = AbstractCard.RAW_W;
        final float ch = AbstractCard.RAW_H;
        final float b_w = 126f;
        final float b_h = 85f;
        final float y = -ch * 0.04f;
        final ColoredTexture panel = GetPanelByRarity(card, leftAlign);
        final Color panelColor = Color.WHITE.cpy().lerp(card.GetRarityColor(true), 0.3f);

        BitmapFont largeFont = RenderHelpers.GetLargeAttributeFont(card);
        largeFont.getData().setScale(card.isPopup ? 0.5f : 1);
        layout.setText(largeFont, mainText.text);

        float text_width = layout.width;
        float suffix_width = 0;

        if (suffix != null)
        {
            layout.setText(largeFont, suffix);
            suffix_width = (layout.width * suffix_scale);
        }

        largeFont = RenderHelpers.GetLargeAttributeFont(card);

        if (leftAlign)
        {
            final float icon_x = -cw * 0.45f;
            final float text_x = ((text_width + suffix_width) * 0.5f) - cw * 0.34f;

            if (panel != null)
            {
                RenderHelpers.DrawOnCardAuto(sb, card, panel, -cw * 0.33f, y, b_w, b_h);
            }

            RenderHelpers.DrawOnCardAuto(sb, card, icon, icon_x, y, 48, 48);
            RenderHelpers.WriteOnCard(sb, card, largeFont, mainText.text, text_x - suffix_width, y, mainText.color, true);

            if (suffix != null)
            {
                largeFont.getData().setScale(largeFont.getScaleX() * suffix_scale);
                RenderHelpers.WriteOnCard(sb, card, largeFont, suffix, text_x + (text_width * (1 - suffix_scale)), y, mainText.color, true);
            }

            if (iconTag != null)
            {
                BitmapFont smallFont = RenderHelpers.GetSmallAttributeFont(card);
                RenderHelpers.WriteOnCard(sb, card, smallFont, iconTag, icon_x, y - 12, Settings.CREAM_COLOR, true);
                RenderHelpers.ResetFont(smallFont);
            }
        }
        else
        {
            final float icon_x = +cw * 0.45f;
            final float text_x = -((text_width + suffix_width) * 0.5f) + cw * 0.34f;

            if (panel != null)
            {
                RenderHelpers.DrawOnCardAuto(sb, card, panel, +cw * 0.33f, y, b_w, b_h);
            }

            RenderHelpers.DrawOnCardAuto(sb, card, icon, icon_x, y, 48, 48);
            RenderHelpers.WriteOnCard(sb, card, largeFont, mainText.text, text_x + suffix_width, y, mainText.color, true);

            if (suffix != null)
            {
                largeFont.getData().setScale(largeFont.getScaleX() * suffix_scale);
                RenderHelpers.WriteOnCard(sb, card, largeFont, suffix, text_x - (text_width * (1 - suffix_scale)), y, mainText.color, true);
            }

            if (iconTag != null)
            {
                BitmapFont smallFont = RenderHelpers.GetSmallAttributeFont(card);
                RenderHelpers.WriteOnCard(sb, card, smallFont, iconTag, icon_x, y - 12, Settings.CREAM_COLOR, true);
                RenderHelpers.ResetFont(smallFont);
            }
        }

        RenderHelpers.ResetFont(largeFont);
    }

    protected ColoredTexture GetPanelByRarity(EYBCard card, boolean leftAlign)
    {
        if (GR.Animator.Config.SimplifyCardUI.Get())
        {
            return null;
        }

        HashMap<AbstractCard.CardRarity, ColoredTexture> map = leftAlign ? leftPanels : rightPanels;
        ColoredTexture result = map.getOrDefault(card.rarity, null);
        if (result == null)
        {
            result = new ColoredTexture((leftAlign ?
            GR.Common.Images.Panel_Skewed_Left : GR.Common.Images.Panel_Skewed_Right).Texture(),
            Color.WHITE.cpy().lerp(card.GetRarityColor(true), 0.25f));
            map.put(card.rarity, result);
        }

        return result;
    }
}