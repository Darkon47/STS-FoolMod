package patches.abstractCard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.unnamed.UnnamedImages;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

@SpirePatch(clz= AbstractCard.class, method="renderEnergy")
public class AbstractCard_RenderEnergy
{
    private static final FieldInfo<Boolean> _darken = JavaUtilities.GetField("darken", AbstractCard.class);
    private static final TextureAtlas.AtlasRegion Orb2A = UnnamedImages.ORB_2_ATLAS.findRegion(UnnamedImages.ORB_2A_PNG);

    @SpirePostfixPatch
    public static void Method(AbstractCard __instance, SpriteBatch sb)
    {
        UnnamedCard card = JavaUtilities.SafeCast(__instance, UnnamedCard.class);
        if (card != null && card.masteryCost > -2 && !_darken.Get(card) && !card.isLocked && card.isSeen)
        {
            Color costColor = Color.WHITE.cpy();

            if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(card) && !CombatStats.Void.CanUse(card))
            {
                costColor = new Color(1f, 0.3f, 0.3f, 1f); //AbstractCard.ENERGY_COST_RESTRICTED_COLOR;
            }

            costColor.a = card.transparency;

            FontHelper.cardEnergyFont_L.getData().setScale(card.drawScale);
            renderHelper(sb, Orb2A, card.current_x, card.current_y, card);
            FontHelper.renderRotatedText(sb, FontHelper.cardEnergyFont_L, card.getMasteryCostString(), card.current_x, card.current_y,
                    138f * card.drawScale * Settings.scale, 190f * card.drawScale * Settings.scale,
                    card.angle, false, costColor);
        }
    }

    private static void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY, AbstractCard card)
    {
        sb.setColor(Color.WHITE);
        sb.draw(img, drawX + img.offsetX - img.originalWidth / 2f, drawY + img.offsetY - img.originalHeight / 2f,
                img.originalWidth / 2f - img.offsetX, img.originalHeight / 2f - img.offsetY,
                img.packedWidth, img.packedHeight, card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
    }
}
