package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.ui.controls.GUI_Image;

public class RenderHelpers
{
    public static final BitmapFont CardDescriptionFont_Normal = GenerateFont(FontHelper.cardDescFont_L, 23, 0, 1);
    public static final BitmapFont CardDescriptionFont_Large = FontHelper.SCP_cardDescFont;
    public static final BitmapFont CardIconFont_Large = GenerateFont(FontHelper.cardDescFont_L, 38, 2.25f, 0.7f);
    public static final BitmapFont CardIconFont_Small = GenerateFont(FontHelper.cardDescFont_L, 19, 1f, 0.3f);


    public static BitmapFont GenerateFont(BitmapFont source, float size, float borderWidth, float shadowOffset)
    {
        return GenerateFont(source, size, borderWidth, new Color(0F, 0F, 0F, 1F), shadowOffset, new Color(0.0F, 0.0F, 0.0F, 0.5F));
    }

    public static BitmapFont GenerateFont(BitmapFont source, float size, float borderWidth, Color borderColor, float shadowOffset, Color shadowColor)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.hinting = FreeTypeFontGenerator.Hinting.Slight;
        param.spaceX = 0;
        param.kerning = true;
        param.borderColor = borderColor;
        param.borderWidth = borderWidth * Settings.scale;
        param.gamma = 0.9F;
        param.borderGamma = 0.9F;
        param.shadowColor = shadowColor;
        param.shadowOffsetX = Math.round(shadowOffset * Settings.scale);
        param.shadowOffsetY = Math.round(shadowOffset * Settings.scale);
        param.borderStraight = false;
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(size * Settings.scale);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(source.getData().fontFile); // TitleFontSize.fontFile
        g.scaleForPixelHeight(param.size);
        BitmapFont font = g.generateFont(param);
        font.setUseIntegerPositions(false);
        font.getData().markupEnabled = false;
        if (LocalizedStrings.break_chars != null)
        {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        return font;
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F,
                img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY,
                img.packedWidth, img.packedHeight, card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        int width = img.getWidth();
        int height = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, width, height, false, false);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height)
    {
        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, new Vector2(drawX, drawY), width, height);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);

        DrawOnCardCentered(sb, card, CopyColor(card, Color.WHITE), img, card.current_x + offset.x, card.current_y + offset.y, width, height);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, CopyColor(card, Color.WHITE), img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, img.getWidth(), img.getHeight());
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX, drawY, 0, 0, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, srcWidth, srcHeight, false, false);
    }

    public static void Draw(SpriteBatch sb, Texture img, float drawX, float drawY, float size)
    {
        Draw(sb, img, Color.WHITE, drawX, drawY, size, size);
    }

    public static void Draw(SpriteBatch sb, Texture img, float x, float y, float width, float height)
    {
        Draw(sb, img, Color.WHITE, x, y, width, height);
    }

    public static void Draw(SpriteBatch sb, Texture img, Color color, float x, float y, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, x, y, 0, 0, width, height, Settings.scale, Settings.scale, 0, 0, 0,
                srcWidth, srcHeight, false, false);
    }

    public static void WriteOnCard(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color)
    {
        WriteOnCard(sb, card, font, text, x, y, color, false);
    }

    public static void WriteOnCard(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color, boolean roundY)
    {
        final float scale = card.drawScale * Settings.scale;
        color.a = card.transparency;
        FontHelper.renderRotatedText(sb, font, text, card.current_x, card.current_y, x * scale, y * scale, card.angle, roundY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color, float scale)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color, scale);
    }

    public static GUI_Image ForTexture(Texture texture)
    {
        return ForTexture(texture, Color.WHITE);
    }

    public static GUI_Image ForTexture(Texture texture, Color color)
    {
        return new GUI_Image(texture, color);
    }

    public static Color CopyColor(AbstractCard card, Color color)
    {
        Color result = color.cpy();
        result.a = card.transparency;
        return result;
    }

    public static ColoredString GetCardAttributeString(EYBCard card, char attributeID)
    {
        switch (attributeID)
        {
            case 'D': return card.GetDamageString();
            case 'B': return card.GetBlockString();
            case 'M': return card.GetMagicNumberString();
            case 'S': return card.GetSecondaryValueString();
            default: return new ColoredString("?", Settings.RED_TEXT_COLOR);
        }
    }
}
