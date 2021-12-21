package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLCardPreview
{
    public PCLCardBase defaultPreview;
    public PCLCardBase upgradedPreview;
    public boolean isMultiPreview;

    public PCLCardPreview(PCLCardBase card, boolean upgrade)
    {
        this.defaultPreview = card;
        this.defaultPreview.isPreview = true;

        if (upgrade)
        {
            this.upgradedPreview = (PCLCardBase) defaultPreview.makeStatEquivalentCopy();
            this.upgradedPreview.isPreview = true;
            this.upgradedPreview.upgrade();
            this.upgradedPreview.displayUpgrades();
        }
    }

    public PCLCardBase GetPreview(boolean upgraded)
    {
        return upgraded && upgradedPreview != null ? upgradedPreview : defaultPreview;
    }

    public void Render(SpriteBatch sb, PCLCardBase card, boolean upgraded)
    {
        PCLCardBase preview = GetPreview(upgraded);

        if (card.isPopup)
        {
            preview.current_x = (float) Settings.WIDTH * 0.2f - 10f * Settings.scale;
            preview.current_y = (float) Settings.HEIGHT * 0.25f;
            preview.drawScale = 1f;
            preview.render(sb);
        }
        else if (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard)
        {
            final float offset_y = (AbstractCard.IMG_HEIGHT * 0.5f - AbstractCard.IMG_HEIGHT * 0.4f) * card.drawScale;
            final float offset_x = (AbstractCard.IMG_WIDTH * 0.5f + AbstractCard.IMG_WIDTH * 0.4f + 16f) * ((card.current_x > Settings.WIDTH * 0.7f) ? card.drawScale : -card.drawScale);

            preview.current_x = card.current_x + offset_x;
            preview.current_y = card.current_y + offset_y;
            preview.drawScale = card.drawScale * 0.8f;
            preview.render(sb);
        }

        if (isMultiPreview)
        {
            String cyclePreviewText = GR.PCL.Strings.Misc.PressKeyToCycle(PCLHotkeys.cycle.getKeyString());
            BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetDescriptionFont(preview, 0.9f);
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, preview, GR.PCL.Images.Panel.Texture(), new Vector2(0, -AbstractCard.RAW_H * 0.55f),
            AbstractCard.IMG_WIDTH * 0.6f, font.getLineHeight() * 1.8f, Color.DARK_GRAY, 0.75f, 1);
            PCLRenderHelpers.WriteOnCard(sb, preview, font, cyclePreviewText, 0, -AbstractCard.RAW_H * 0.55f, Color.MAGENTA);
            pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
        }
    }
}
