package pinacolada.cards.base.cardTextParsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.ColoredString;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class ConditionToken extends CTToken
{
    private ColoredString coloredString = new ColoredString(null, null);

    private ConditionToken(String text)
    {
        super(CTTokenType.Text, text);
    }

    public static int TryAdd(CTContext parser)
    {
        if (parser.character == '@' && parser.remaining > 1)
        {
            builder.setLength(0);

            int i = 1;
            while (true)
            {
                final Character next = parser.NextCharacter(i);
                if (next == null)
                {
                    break;
                }
                else if (next == '@')
                {

                    final String text = builder.toString();
                    ConditionToken token = new ConditionToken(text);
                    if (parser.card != null) {
                        boolean inBattle = PCLGameUtilities.InBattle();
                        boolean conditionMet = parser.card.CheckPrimaryCondition(false);
                        token.coloredString.SetColor(inBattle && conditionMet ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR);
                        if (inBattle && !conditionMet) {
                            token.coloredString.color.a = 0.6f;
                        }
                    }
                    parser.AddToken(token);

                    return i + 1;
                }
                else
                {
                    builder.append(next);
                }

                i += 1;
            }
        }

        return 0;
    }

    @Override
    public void Render(SpriteBatch sb, CTContext context)
    {
        if (coloredString.text == null || GR.UI.Elapsed25())
        {
            UpdateString(context);
        }
        super.Render(sb, context, coloredString);
    }

    private void UpdateString(CTContext context)
    {
        coloredString.text = this.rawText;
        coloredString.SetColor(PCLGameUtilities.InBattle() && context.card.CheckPrimaryCondition(false) ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR);
    }
}