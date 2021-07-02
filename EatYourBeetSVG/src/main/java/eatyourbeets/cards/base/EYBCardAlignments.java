package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class EYBCardAlignments
{
    public final ArrayList<EYBCardAlignment> List = new ArrayList<>();
    public EYBCardAlignment Star = null;

    public EYBCardAlignments()
    {

    }

    public boolean CanSynergize(EYBCardAlignments other)
    {
        for (EYBCardAlignment a : List)
        {
            for (EYBCardAlignment b : other.List)
            {
                if (a.Type == b.Type && (a.level > 1 || b.level > 1))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void Set(int red, int green, int blue, int light, int dark)
    {
        Add(EYBCardAlignmentType.Red, red);
        Add(EYBCardAlignmentType.Green, green);
        Add(EYBCardAlignmentType.Blue, blue);
        Add(EYBCardAlignmentType.Light, light);
        Add(EYBCardAlignmentType.Dark, dark);
    }

    public void SetStar(int level)
    {
        if (level == 0)
        {
            Star = null;
        }
        else if (Star != null)
        {
            Star.level = level;
        }
        else
        {
            Star = new EYBCardAlignment(EYBCardAlignmentType.Star, level);
        }
    }

    public boolean HasStar()
    {
        return Star != null;
    }

    public void Add(EYBCardAlignments other)
    {
        for (EYBCardAlignment item : other.List)
        {
            Add(item.Type, item.level);
        }
    }

    public void Add(EYBCardAlignmentType type, int level)
    {
        if (level > 0)
        {
            for (int i = 0; i < List.size(); i++)
            {
                EYBCardAlignment a = List.get(i);
                if (a.Type == type)
                {
                    if ((a.level += level) <= 0)
                    {
                        List.remove(a);
                    }
                    else
                    {
                        List.sort(EYBCardAlignment::compareTo);
                    }

                    return;
                }
            }

            List.add(new EYBCardAlignment(type, level));
            List.sort(EYBCardAlignment::compareTo);
        }
    }

    public void Set(EYBCardAlignmentType type, int level)
    {
        for (int i = 0; i < List.size(); i++)
        {
            EYBCardAlignment a = List.get(i);
            if (a.Type == type)
            {
                if ((a.level = level) <= 0)
                {
                    List.remove(a);
                }
                else
                {
                    List.sort(EYBCardAlignment::compareTo);
                }

                return;
            }
        }

        if (level > 0)
        {
            List.add(new EYBCardAlignment(type, level));
            List.sort(EYBCardAlignment::compareTo);
        }
    }

    public EYBCardAlignment Get(EYBCardAlignmentType type)
    {
        for (EYBCardAlignment item : List)
        {
            if (item.Type == type)
            {
                return item;
            }
        }

        return null;
    }

    public int GetLevel(EYBCardAlignmentType type)
    {
        for (EYBCardAlignment item : List)
        {
            if (item.Type == type)
            {
                return item.level;
            }
        }

        return 0;
    }

    public void RenderOnCard(SpriteBatch sb, EYBCard card, boolean inHand)
    {
        float size;
        float y = AbstractCard.RAW_H;
        float offsetX = 0;

        if (inHand)
        {
            size = 42;
            y *= 0.57f;
        }
        else
        {
            size = 36;
            y *= -0.47f;
        }

        if (HasStar())
        {
            Star.RenderOnCard(sb, card, 0, y, size);
            return;
        }

        int half = List.size() / 2;
        for (int i = 0; i < List.size(); i++)
        {
            final EYBCardAlignment item = List.get(i);
            float x = AbstractCard.RAW_W;
            if (List.size() % 2 == 1)
            {
                x *= (0.14 * (i - half));
            }
            else
            {
                x *= 0.07f + (0.14 * (i - half));
            }

            if (!inHand)
            {
                x *= 0.85f;
            }

            item.RenderOnCard(sb, card, x, y, size);
        }
    }
}