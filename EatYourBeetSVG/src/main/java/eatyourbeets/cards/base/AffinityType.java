package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.RenderHelpers;

public enum AffinityType implements Comparable<AffinityType>
{
    Red(0, "Red", GR.Common.Images.Affinities.Red),
    Green(1, "Green", GR.Common.Images.Affinities.Green),
    Blue(2, "Blue", GR.Common.Images.Affinities.Blue),
    Orange(3, "Orange", GR.Common.Images.Affinities.Orange),
    Light(4, "Light", GR.Common.Images.Affinities.Light),
    Dark(5, "Dark", GR.Common.Images.Affinities.Dark),
    Star(-1, "Star", GR.Common.Images.Affinities.Star),
    General(-2, "General", GR.Common.Images.Affinities.General);// Don't use directly

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.Common.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.Common.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    protected static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;
    protected static final AffinityType[] BASIC_TYPES = new AffinityType[6];
    protected static final AffinityType[] ALL_TYPES = new AffinityType[7];

    static
    {
        ALL_TYPES[0] = Star;
        ALL_TYPES[1] = BASIC_TYPES[0] = Red;
        ALL_TYPES[2] = BASIC_TYPES[1] = Green;
        ALL_TYPES[3] = BASIC_TYPES[2] = Blue;
        ALL_TYPES[4] = BASIC_TYPES[3] = Orange;
        ALL_TYPES[5] = BASIC_TYPES[4] = Light;
        ALL_TYPES[6] = BASIC_TYPES[5] = Dark;
    }

    public static AffinityType[] BasicTypes()
    {
        return BASIC_TYPES;
    }

    public static AffinityType[] AllTypes()
    {
        return ALL_TYPES;
    }

    public final int ID;
    public final TextureCache Icon;
    public final String Symbol;

    AffinityType(int id, String symbol, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
        this.Symbol = symbol;
    }

    public Texture GetIcon()
    {
        return Icon.Texture();
    }

    public Texture GetBorder(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderLV2 : BorderLV1).Texture();
    }

    public Texture GetBackground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderBG.Texture() : null);
    }

    public Texture GetForeground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderFG.Texture() : null);
    }

    public TextureRegion GetPowerIcon()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Force.icon;

            case Green: return GR.Tooltips.Agility.icon;

            case Blue: return GR.Tooltips.Intellect.icon;

            case Orange: return GR.Tooltips.Willpower.icon;

            case Light: return GR.Tooltips.Blessing.icon;

            case Dark: return GR.Tooltips.Corruption.icon;

            case Star: default: return null;
        }
    }

    public Color GetAlternateColor(float lerp)
    {
        return Color.WHITE.cpy().lerp(GetAlternateColor(), lerp);
    }

    public Color GetAlternateColor()
    {
        switch (this)
        {
            case Red: return new Color(0.8f, 0.5f, 0.5f, 1f);

            case Green: return new Color(0.45f, 0.7f, 0.55f, 1f);

            case Blue: return new Color(0.45f, 0.55f, 0.7f, 1f);

            case Orange: return new Color(0.7f, 0.6f, 0.5f, 1f);

            case Light: return new Color(0.8f, 0.8f, 0.3f, 1f);

            case Dark: return new Color(0.55f, 0.1f, 0.85f, 1);//0.7f, 0.55f, 0.7f, 1f);

            case Star: default: return new Color(0.25f, 0.25f, 0.25f, 1f);
        }
    }

    public static AffinityType FromTooltip(EYBCardTooltip tooltip)
    {   //@Formatter: Off
        if (tooltip == GR.Tooltips.Affinity_Red    ) { return AffinityType.Red;     }
        if (tooltip == GR.Tooltips.Affinity_Green  ) { return AffinityType.Green;   }
        if (tooltip == GR.Tooltips.Affinity_Blue   ) { return AffinityType.Blue;    }
        if (tooltip == GR.Tooltips.Affinity_Light  ) { return AffinityType.Light;   }
        if (tooltip == GR.Tooltips.Affinity_Dark   ) { return AffinityType.Dark;    }
        if (tooltip == GR.Tooltips.Affinity_Star   ) { return AffinityType.Star;    }
        if (tooltip == GR.Tooltips.Affinity_General) { return AffinityType.General; }
        return null;
    }   //@Formatter: On

    public void Render(int level, SpriteBatch sb, Color color, float cX, float cY, float size)
    {
        Texture background = GetBackground(level);
        if (background != null)
        {
            RenderHelpers.DrawCentered(sb, color, background, cX, cY, size, size, 1, 0);
        }

        RenderHelpers.DrawCentered(sb, color, GetIcon(), cX, cY, size, size, 1, 0);

        Texture border = GetBorder(level);
        if (border != null)
        {
            RenderHelpers.DrawCentered(sb, color, border, cX, cY, size, size, 1, 0);
        }

        Texture foreground = GetForeground(level);
        if (foreground != null)
        {
            RenderHelpers.DrawCentered(sb, color, foreground, cX, cY, size, size, 1, 0);
        }

        if (this == AffinityType.Star)
        {
            RenderHelpers.DrawCentered(sb, color, GR.Common.Images.Affinities.Star_FG.Texture(), cX, cY, size, size, 1, 0);
        }
    }
}