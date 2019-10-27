package patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import eatyourbeets.cards.EYBCard;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;

@SpirePatch(clz= CardGlowBorder.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
public class CardGlowBorderPatch
{
    private static Field<Color> colorField = Utilities.GetPrivateField("color", AbstractGameEffect.class);

    public static Color overrideColor;

    @SpirePostfixPatch
    public static void Method(CardGlowBorder __instance, AbstractCard card)
    {
        if (overrideColor != null)
        {
            Color color = colorField.Get(__instance);
            if (color != null)
            {
                color.r = overrideColor.r;
                color.g = overrideColor.g;
                color.b = overrideColor.b;
            }

            return;
        }

        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            if (c instanceof AnimatorCard && ((AnimatorCard)c).HasActiveSynergy())
            {
                Color color = colorField.Get(__instance);
                if (color != null)
                {
                    color.r = Color.GOLD.r;
                    color.g = Color.GOLD.g;
                    color.b = Color.GOLD.b;
                }
            }
            else if (c instanceof UnnamedCard && ((UnnamedCard)c).isDepleted())
            {
                Color color = colorField.Get(__instance);
                if (color != null)
                {
                    color.r = Color.LIGHT_GRAY.r;
                    color.g = Color.LIGHT_GRAY.g;
                    color.b = Color.LIGHT_GRAY.b;
                }
            }
        }
    }
}