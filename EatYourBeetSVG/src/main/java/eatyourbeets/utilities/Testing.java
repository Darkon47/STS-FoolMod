package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Testing
{
    private static int colorIndex = 0;
    private static int fontIndex = 0;
    private static final ArrayList<Field> fonts = new ArrayList<>();
    private static final ArrayList<Field> colors = new ArrayList<>();

    public static BitmapFont GetRandomFont()
    {
        if (fonts.size() == 0)
        {
            for (Field field : FontHelper.class.getDeclaredFields())
            {
                if (field.getType() == BitmapFont.class)
                {
                    field.setAccessible(true);
                    fonts.add(field);
                }
            }
        }

        if (fontIndex >= fonts.size())
        {
            fontIndex = 0;
        }

        Field field = fonts.get(fontIndex++);
        try
        {
            JavaUtilities.Log(Testing.class, field.getName());

            return (BitmapFont) field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Color GetRandomColor()
    {
        if (colors.size() == 0)
        {
            for (Field field : Color.class.getDeclaredFields())
            {
                if (field.getType() == Color.class)
                {
                    field.setAccessible(true);
                    colors.add(field);
                }
            }

            for (Field field : Settings.class.getDeclaredFields())
            {
                if (field.getType() == Color.class)
                {
                    field.setAccessible(true);
                    colors.add(field);
                }
            }
        }

        if (colorIndex >= colors.size())
        {
            colorIndex = 0;
        }

        Field field = colors.get(colorIndex++);
        try
        {
            JavaUtilities.Log(Testing.class, field.getName());

            return (Color) field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
