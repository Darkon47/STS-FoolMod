package eatyourbeets.misc.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NewLineToken extends CTToken
{
    protected static NewLineToken Default = new NewLineToken(" NL ");

    protected NewLineToken(Object text)
    {
        this.type = CTTokenType.NewLine;
        this.text = String.valueOf(text);
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == ' ' && parser.CompareNext(1, 'N') && parser.CompareNext(2, 'L') && parser.CompareNext(3, ' '))
        {
            parser.AddToken(Default);

            return 4;
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        throw new RuntimeException("New line token should not be rendered");
    }
}