package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.JUtils;

public enum Affinity implements Comparable<Affinity>
{
    Red(0, "Red", "R", GR.Common.Images.Affinities.Red),
    Green(1, "Green", "G", GR.Common.Images.Affinities.Green),
    Blue(2, "Blue", "B", GR.Common.Images.Affinities.Blue),
    Orange(3, "Orange", "O", GR.Common.Images.Affinities.Orange),
    Light(4, "Light", "L", GR.Common.Images.Affinities.Light),
    Dark(5, "Dark", "D", GR.Common.Images.Affinities.Dark),
    Silver(6, "Silver", "S", GR.Common.Images.Affinities.Silver),
    Star(-1, "Star", "A", GR.Common.Images.Affinities.Star),
    General(-2, "Gen", "W", GR.Common.Images.Affinities.General);// Don't use directly

    public static final int MAX_ID = 4;

    protected static final TextureCache BorderBG = GR.Common.Images.Affinities.BorderBG;
    protected static final TextureCache BorderFG = GR.Common.Images.Affinities.BorderFG;
    protected static final TextureCache BorderLV2 = GR.Common.Images.Affinities.Border;
    protected static final TextureCache BorderLV1 = GR.Common.Images.Affinities.Border_Weak;
    protected static final Affinity[] BASIC_TYPES = new Affinity[6];
    protected static final Affinity[] EXTENDED_TYPES = new Affinity[7];
    protected static final Affinity[] ALL_TYPES = new Affinity[8];

    static
    {
        ALL_TYPES[0] = EXTENDED_TYPES[0] = BASIC_TYPES[0] = Red;
        ALL_TYPES[1] = EXTENDED_TYPES[1] = BASIC_TYPES[1] = Green;
        ALL_TYPES[2] = EXTENDED_TYPES[2] = BASIC_TYPES[2] = Blue;
        ALL_TYPES[3] = EXTENDED_TYPES[3] = BASIC_TYPES[3] = Orange;
        ALL_TYPES[4] = EXTENDED_TYPES[4] = BASIC_TYPES[4] = Light;
        ALL_TYPES[5] = EXTENDED_TYPES[5] = BASIC_TYPES[5] = Dark;
        ALL_TYPES[6] = EXTENDED_TYPES[6] = Silver;
        ALL_TYPES[7] = Star;
    }

    public static Affinity[] Basic()
    {
        return BASIC_TYPES;
    }

    public static Affinity[] Extended()
    {
        return EXTENDED_TYPES;
    }

    public static Affinity[] All()
    {
        return ALL_TYPES;
    }

    public final int ID;
    public final TextureCache Icon;
    public final String Name;
    public final String PowerSymbol;

    Affinity(int id, String name, String powerSymbol, TextureCache icon)
    {
        this.ID = id;
        this.Icon = icon;
        this.Name = name;
        this.PowerSymbol = powerSymbol;
    }

    public Texture GetIcon()
    {
        return Icon.Texture();
    }

    public Texture GetBorder(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderLV2 : BorderLV1).Texture();
    }

    public Texture GetBackground(int level, int upgrade)
    {
        return /*this == Star ? null : */((level + upgrade) > 1 ? BorderBG.Texture() : null);
    }

    public Texture GetForeground(int level)
    {
        return /*this == Star ? null : */(level > 1 ? BorderFG.Texture() : null);
    }

    public TextureRegion GetPowerIcon()
    {
        return GetPowerTooltip().icon;
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

            case Silver: return new Color(0.6f, 0.6f, 0.6f, 1f);

            case Star: default: return new Color(0.95f, 0.95f, 0.95f, 1f);
        }
    }

    public String GetAffinitySymbol() {
        return JUtils.Format("A-{0}", Name);
    }

    public String GetFormattedAffinitySymbol() {
        return JUtils.Format("[{0}]", GetAffinitySymbol());
    }

    public String GetFormattedPowerSymbol() {
        return JUtils.Format("[{0}]", PowerSymbol);
    }

    public static Affinity FromTooltip(EYBCardTooltip tooltip)
    {   //@Formatter: Off
        if (tooltip.Is(GR.Tooltips.Affinity_Red)    ) { return Affinity.Red;     }
        if (tooltip.Is(GR.Tooltips.Affinity_Green)  ) { return Affinity.Green;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Blue)   ) { return Affinity.Blue;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Orange) ) { return Affinity.Orange;  }
        if (tooltip.Is(GR.Tooltips.Affinity_Light)  ) { return Affinity.Light;   }
        if (tooltip.Is(GR.Tooltips.Affinity_Dark)   ) { return Affinity.Dark;    }
        if (tooltip.Is(GR.Tooltips.Affinity_Silver) ) { return Affinity.Silver;  }
        if (tooltip.Is(GR.Tooltips.Affinity_Star)   ) { return Affinity.Star;    }
        if (tooltip.Is(GR.Tooltips.Affinity_General)) { return Affinity.General; }
        return null;
    }   //@Formatter: On

    public EYBCardTooltip GetTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Affinity_Red;
            case Green: return GR.Tooltips.Affinity_Green;
            case Blue: return GR.Tooltips.Affinity_Blue;
            case Orange: return GR.Tooltips.Affinity_Orange;
            case Light: return GR.Tooltips.Affinity_Light;
            case Dark: return GR.Tooltips.Affinity_Dark;
            case Silver: return GR.Tooltips.Affinity_Silver;
            case Star: return GR.Tooltips.Affinity_Star;
            case General: return GR.Tooltips.Affinity_General;
            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }

    public EYBCardTooltip GetPowerTooltip()
    {
        switch (this)
        {
            case Red: return GR.Tooltips.Might;
            case Green: return GR.Tooltips.Velocity;
            case Blue: return GR.Tooltips.Wisdom;
            case Orange: return GR.Tooltips.Endurance;
            case Light: return GR.Tooltips.Supercharge;
            case Dark: return GR.Tooltips.Desecration;
            case Silver: return GR.Tooltips.Technic;
            case Star:
            case General:
                return GR.Tooltips.Affinity_Star;
            default: throw new EnumConstantNotPresentException(Affinity.class, this.name());
        }
    }
}