package eatyourbeets.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class CTContext
{
    protected final static float IMG_HEIGHT = 420.0F * Settings.scale;
    protected final static float IMG_WIDTH = 300.0F * Settings.scale;
    protected final static float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;
    protected final static float DESC_OFFSET_Y = Settings.BIG_TEXT_MODE ? IMG_HEIGHT * 0.24F : IMG_HEIGHT * 0.255F;
    protected final static float CN_DESC_BOX_WIDTH = IMG_WIDTH * 0.72F;
    protected final static Color DEFAULT_COLOR = Settings.CREAM_COLOR.cpy();

    public final ArrayList<CTLine> lines = new ArrayList<>();

    protected Character character;
    protected BitmapFont font;
    protected CharSequence text;
    protected int remaining;
    protected int characterIndex;
    protected int lineIndex;
    protected float scaleModifier;

    public EYBCard card;
    public float start_y;
    public float start_x;
    public Color color;

    public void Initialize(EYBCard card, String text)
    {
        this.font = RenderHelpers.CardDescriptionFont_Normal;
        this.card = card;
        this.text = text;
        this.lines.clear();
        this.scaleModifier = 1;

        AddLine();

        this.characterIndex = 0;
        this.lineIndex = 0;

        if (card != null)
        {
            this.card.tooltips.clear();

            final float max = text.contains("。") ? 60f : 100f; // There is a 99.99% chance that a card text in zhs/zht will contain '。'
            if (text.length() > max)
            {
                int actualLength = text.replace(" NL ", "").length();
                if (actualLength > max)
                {
                    scaleModifier -= (0.12f * (actualLength / max));
                }
            }

            font.getData().setScale(scaleModifier);
        }

        int amount = 0;
        while (MoveIndex(amount))
        {
            this.character = this.text.charAt(characterIndex);

            // The order matters!
            if ((amount = VariableToken.TryAdd(this))    == 0 // !M!
            &&  (amount = SymbolToken.TryAdd(this))      == 0 // [E]
            &&  (amount = SpecialToken.TryAdd(this))     == 0 // {code}
            &&  (amount = NewLineToken.TryAdd(this))     == 0 // NL
            &&  (amount = WhitespaceToken.TryAdd(this))  == 0 //
            &&  (amount = PunctuationToken.TryAdd(this)) == 0 // .,-.:; etc
            &&  (amount = WordToken.TryAdd(this))        == 0)// Letters/Digits
            {
                JavaUtilities.GetLogger(this).error("Error parsing card text, Character: " + character + ", Text: " + this.text);
                amount = 1;
            }
        }

        this.lines.get(lineIndex).TrimEnd(); // Remove possible whitespace from the last line

        if (card != null)
        {
            RenderHelpers.ResetFont(font);
        }
    }

    public void Render(SpriteBatch sb)
    {
        font = RenderHelpers.GetDescriptionFont(card, scaleModifier);

        float height = 0;
        for (CTLine line : lines)
        {
            height += line.CalculateHeight(font);
        }

        this.start_y = (card.current_y - IMG_HEIGHT * card.drawScale * 0.5f + DESC_OFFSET_Y * card.drawScale) + (height * 0.775f + font.getCapHeight() * 0.375F) -6f;
        this.start_x = 0;
        this.lineIndex = 0;
        this.color = RenderHelpers.CopyColor(card, DEFAULT_COLOR);

        for (lineIndex = 0; lineIndex < lines.size(); lineIndex += 1)
        {
            lines.get(lineIndex).Render(sb);
        }

        RenderHelpers.ResetFont(font);
    }

    protected boolean CompareNext(int amount, char character)
    {
        Character other = NextCharacter(amount);
        if (other != null)
        {
            return other == character;
        }

        return false;
    }

    protected Character NextCharacter(int amount)
    {
        if (amount > remaining)
        {
            return null;
        }

        return text.charAt(characterIndex + amount);
    }

    protected boolean MoveIndex(int amount)
    {
        characterIndex += amount;
        remaining = text.length() - characterIndex - 1;

        return remaining >= 0;
    }

    protected CTLine AddLine()
    {
        CTLine line = new CTLine(this);

        lines.add(line);
        lineIndex += 1;

        return line;
    }

    protected void AddToken(CTToken token)
    {
        if (card == null)
        {
            // if card is null just add all tokens to the first line
            lines.get(0).tokens.add(token);
        }
        else if (token.type == CTTokenType.NewLine)
        {
            AddLine();
        }
        else
        {
            lines.get(lineIndex).Add(token);
        }
    }

    protected void AddTooltip(EYBCardTooltip tooltip)
    {
        if (card != null && !card.tooltips.contains(tooltip) && GR.Tooltips.CanAdd(tooltip))
        {
            card.tooltips.add(tooltip);
        }
    }
}