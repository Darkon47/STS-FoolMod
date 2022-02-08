package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLRenderHelpers;

public class CardRewardBundle
{
    private static final PCLStrings.Rewards text = PGR.PCL.Strings.Rewards;
    public final AbstractCard card;
    public final ActionT1<CardRewardBundle> onSelect;
    public float textOffsetX;
    public float textOffsetY;
    public float iconOffsetX;
    public float iconOffsetY;
    public Color textColor;
    public String title;
    public Texture icon;
    public String tooltipHeader;
    public String tooltipBody;
    public Hitbox tooltipHB;
    public int amount;

    public CardRewardBundle(AbstractCard card, ActionT1<CardRewardBundle> onSelect)
    {
        this.card = card;
        this.onSelect = onSelect;
        this.tooltipHB = new Hitbox(0, 0, AbstractCard.RAW_W, AbstractCard.RAW_H);
    }

    public CardRewardBundle SetIcon(Texture icon, float iconOffsetX, float iconOffsetY)
    {
        this.icon = icon;
        this.iconOffsetX = iconOffsetX;
        this.iconOffsetY = iconOffsetY;

        return this;
    }

    public CardRewardBundle SetText(String text, Color textColor, float textOffsetX, float textOffsetY)
    {
        this.title = text;
        this.textColor = textColor.cpy();
        this.textOffsetX = textOffsetX;
        this.textOffsetY = textOffsetY;

        return this;
    }

    public CardRewardBundle SetTooltip(String header, String body)
    {
        this.tooltipHeader = header;
        this.tooltipBody = body;

        return this;
    }

    public CardRewardBundle SetAmount(int amount)
    {
        this.amount = amount;

        return this;
    }

    public void Open()
    {

    }

    public void Update()
    {
        if (tooltipBody != null)
        {
            tooltipHB.resize(card.drawScale * AbstractCard.IMG_WIDTH, card.drawScale * AbstractCard.IMG_HEIGHT * 0.15f);
            tooltipHB.move(card.current_x, card.current_y + (textOffsetY * card.drawScale * Settings.scale));
            tooltipHB.update();

            if (tooltipHB.hovered)
            {
                TipHelper.renderGenericTip(tooltipHB.x + tooltipHB.width * 0.7f, tooltipHB.cY, tooltipHeader, tooltipBody);
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        BitmapFont font = FontHelper.buttonLabelFont;
        font.getData().setScale(card.drawScale * 0.8f);
        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, icon, iconOffsetX, iconOffsetY, icon.getWidth(), icon.getHeight());
        PCLRenderHelpers.WriteOnCard(sb, card, font, title, textOffsetX, textOffsetY, textColor);
        pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
        tooltipHB.render(sb);
    }

    public void Acquired()
    {
        if (onSelect != null)
        {
            onSelect.Invoke(this);
        }
    }
}
