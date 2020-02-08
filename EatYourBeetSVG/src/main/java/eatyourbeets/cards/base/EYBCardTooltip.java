package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class EYBCardTooltip
{
    private final static ArrayList EMPTY_LIST = new ArrayList();

    private final static FieldInfo<String> _body = JavaUtilities.GetField("BODY", TipHelper.class);
    private final static FieldInfo<String> _header = JavaUtilities.GetField("HEADER", TipHelper.class);
    private final static FieldInfo<ArrayList> _card = JavaUtilities.GetField("card", TipHelper.class);
    private final static FieldInfo<ArrayList> _keywords = JavaUtilities.GetField("KEYWORDS", TipHelper.class);
    private final static FieldInfo<ArrayList> _powerTips = JavaUtilities.GetField("POWER_TIPS", TipHelper.class);
    private final static FieldInfo<Boolean> _renderedTipsThisFrame = JavaUtilities.GetField("renderedTipThisFrame", TipHelper.class);

    private static final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float TEXT_OFFSET_X = 22.0F * Settings.scale;
    private static final float HEADER_OFFSET_Y = 12.0F * Settings.scale;
    private static final float ORB_OFFSET_Y = -8.0F * Settings.scale;
    private static final float BODY_OFFSET_Y = -20.0F * Settings.scale;
    private static final float BOX_W = 360.0F * Settings.scale;
    private static final float BODY_TEXT_WIDTH = 320.0F * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
    private static final float POWER_ICON_OFFSET_X = 40.0F * Settings.scale;

    public TextureAtlas.AtlasRegion icon;
    public String title;
    public String description;

    public EYBCardTooltip(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public EYBCardTooltip(Keyword keyword)
    {
        this.title = keyword.PROPER_NAME;
        this.description = keyword.DESCRIPTION;
    }

    public static boolean CanRenderTooltips()
    {
        return !_renderedTipsThisFrame.Get(null);
    }

    public static void RenderAll(SpriteBatch sb, EYBCard card)
    {
        _body.Set(null, null);
        _header.Set(null, null);
        _card.Set(null, null);
        _keywords.Set(null, EMPTY_LIST);
        _powerTips.Set(null, EMPTY_LIST);
        _renderedTipsThisFrame.Set(null, true);

        tooltips.clear();
        card.GenerateDynamicTooltips(tooltips);
        for (EYBCardTooltip tooltip : card.tooltips)
        {
            if (!tooltips.contains(tooltip))
            {
                tooltips.add(tooltip);
            }
        }

        float x;
        float y;
        if (card.isPopup)
        {
            x = 0.78f * Settings.WIDTH;
            y = 0.85f * Settings.HEIGHT;
        }
        else
        {
            x = card.current_x;
            if (card.current_x < (float) Settings.WIDTH * 0.75F)
            {
                x += AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD;
            }
            else
            {
                x -= AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD + BOX_W;
            }

            y = card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H;
            if (tooltips.size() >= 5 && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD)
            {
                y += AbstractCard.IMG_HEIGHT * 0.3f; //(float) (tooltips.size() - 1) * 62.0F * Settings.scale;
            }
        }

        for (EYBCardTooltip tooltip : tooltips)
        {
            y -= tooltip.Render(sb, x, y) + BOX_EDGE_H * 3.15F;
        }
    }

    public float Render(SpriteBatch sb, float x, float y)
    {
        float h = -FontHelper.getSmartHeight(RenderHelpers.CardTooltipFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;

        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

        if (icon != null)
        {
            renderTipEnergy(sb, icon, x + BOX_W - TEXT_OFFSET_X - 28 * Settings.scale, y + ORB_OFFSET_Y, 28, 28);
        }

        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);

//        if (icon != null)
//        {
//            renderTipEnergy(sb, icon, x + TEXT_OFFSET_X, y + ORB_OFFSET_Y);
//
//            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X * 2.5F, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
//        }
//        else
//        {
//            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
//        }

        FontHelper.renderSmartText(sb, RenderHelpers.CardTooltipFont, description, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);

        return h;
    }

    public void renderTipEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y, float width, float height)
    {
        sb.setColor(Color.WHITE);
        sb.draw(region.getTexture(), x + region.offsetX * Settings.scale, y + region.offsetY * Settings.scale, 0.0F, 0.0F,
        width, height, Settings.scale, Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }

    @Override
    public String toString()
    {
        return title;
    }
}
