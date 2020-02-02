package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.cardTextParsing.CTContext;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class EYBCardText
{
    private final static FieldInfo<ArrayList> _keywords = JavaUtilities.GetField("KEYWORDS", TipHelper.class);
    private final static FieldInfo<ArrayList> _powerTips = JavaUtilities.GetField("POWER_TIPS", TipHelper.class);
    private final static FieldInfo<Boolean> _renderedTipsThisFrame = JavaUtilities.GetField("renderedTipThisFrame", TipHelper.class);
    private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final CommonImages.Badges BADGES = GR.Common.Images.Badges;
    private static final CommonImages.CardIcons ICONS = GR.Common.Images.Icons;
    private float badgeAlphaTargetOffset = 1f;
    private float badgeAlphaOffset = -0.2f;

    protected final CTContext context = new CTContext();
    protected final String description;
    protected final String upgradedDescription;
    protected final EYBCard card;
    protected String overrideDescription;

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.description = cardStrings.DESCRIPTION;
        this.upgradedDescription = cardStrings.UPGRADE_DESCRIPTION;
    }

    public void ForceRefresh()
    {
        if (overrideDescription != null)
        {
            card.rawDescription = overrideDescription;
        }
        else if (card.upgraded && upgradedDescription != null)
        {
            card.rawDescription = upgradedDescription;
        }
        else
        {
            card.rawDescription = description;
        }

        context.Initialize(card, card.rawDescription);
    }

    public void OverrideDescription(String description, boolean forceRefresh)
    {
        overrideDescription = description;

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }

    public void RenderDescription(SpriteBatch sb)
    {
        if (card.isFlipped || card.isSeen && !card.isLocked || card.transparency <= 0.1f)
        {
            context.Render(sb);

            RenderAttributes(sb);

            if (card.drawScale > 0.3f)
            {
                RenderBadges(sb);

                ColoredString header = card.GetHeaderText();
                if (header != null)
                {
                    BitmapFont font = RenderHelpers.GetSmallTextFont(card, header.text);
                    RenderHelpers.WriteOnCard(sb, card, font, header.text, 0, AbstractCard.RAW_H * 0.48f, header.color, true);
                    RenderHelpers.ResetFont(font);
                }

                ColoredString bottom = card.GetBottomText();
                if (bottom != null)
                {
                    BitmapFont font = RenderHelpers.GetSmallTextFont(card, bottom.text);
                    RenderHelpers.WriteOnCard(sb, card, RenderHelpers.GetSmallTextFont(card, bottom.text), bottom.text, 0, -AbstractCard.RAW_H * 0.47f, bottom.color, true);
                    RenderHelpers.ResetFont(font);
                }
            }
        }
        else
        {
            FontHelper.menuBannerFont.getData().setScale(card.drawScale * 1.25F);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", card.current_x, card.current_y, 0.0F, -200.0F * Settings.scale * card.drawScale / 2.0F, card.angle, true, Settings.CREAM_COLOR.cpy());
            FontHelper.menuBannerFont.getData().setScale(1.0F);
        }
    }

    public void RenderTooltips(SpriteBatch sb)
    {
        if (!Settings.hideCards && (card.isPopup || card.CanRenderTip()) && !_renderedTipsThisFrame.Get(null))
        {
            if (card.isLocked || !card.isSeen || (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen))
            {
                return;
            }

            _keywords.Get(null).clear();
            _powerTips.Get(null).clear();
            _renderedTipsThisFrame.Set(null, true);

            float x;
            float y;

            if (card.isPopup)
            {
                x = 0.78f * Settings.WIDTH;
                y = 0.85f * Settings.HEIGHT;
            }
            else
            {
                x = card.current_x;
                if (card.current_x < (float) Settings.WIDTH * 0.75F)
                {
                    x += AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD;
                }
                else
                {
                    x -= AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD + BOX_W;
                }

                y = card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H;
                if (context.tooltips.size() >= 4)
                {
                    y += (float) (context.tooltips.size() - 1) * 62.0F * Settings.scale;
                }
            }

            for (EYBCardTooltip tooltip : context.tooltips)
            {
                y -= tooltip.Render(sb, x, y) + BOX_EDGE_H * 3.15F;
            }

            if (card.cardData.previewInitialized)
            {
                card.cardsToPreview = card.cardData.GetCardPreview(card);
                card.renderCardPreview(sb);
            }
        }
    }

    protected void RenderAttributes(SpriteBatch sb)
    {
        boolean leftAlign = true;
        AbstractAttribute temp;
        if ((temp = card.GetDamageInfo()) != null)
        {
            temp.Render(sb, card, leftAlign);
            leftAlign = false;
        }
        if ((temp = card.GetBlockInfo()) != null)
        {
            temp.Render(sb, card, leftAlign);
            leftAlign = false;
        }
        if ((temp = card.GetSpecialInfo()) != null)
        {
            temp.Render(sb, card, leftAlign);
            //leftAlign = false;
        }
    }

    protected void RenderBadges(SpriteBatch sb)
    {
        final float alpha = UpdateBadgeAlpha();

        int offset_y = 0;
        if (card.isInnate)
        {
            offset_y -= RenderBadge(sb, BADGES.Innate.Texture(), offset_y, alpha);
        }
        if (card.isEthereal)
        {
            offset_y -= RenderBadge(sb, BADGES.Ethereal.Texture(), offset_y, alpha);
        }
        if (card.retain || card.selfRetain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), offset_y, alpha);
        }
        if (card.exhaust)
        {
            RenderBadge(sb, BADGES.Exhaust.Texture(), offset_y, alpha);
        }

        offset_y = 0;
        if (card.intellectScaling > 0)
        {
            offset_y -= RenderScaling(sb, ICONS.Intellect.Texture(), card.intellectScaling, offset_y);
        }
        if (card.forceScaling > 0)
        {
            offset_y -= RenderScaling(sb, ICONS.Force.Texture(), card.forceScaling, offset_y);
        }
        if (card.agilityScaling > 0)
        {
            RenderScaling(sb, ICONS.Agility.Texture(), card.agilityScaling, offset_y);
        }
    }

    private float RenderScaling(SpriteBatch sb, Texture texture, float scaling, float y)
    {
        final float offset_x = -AbstractCard.RAW_W * 0.46f;
        final float offset_y = AbstractCard.RAW_H * 0.28f;
        final BitmapFont font = RenderHelpers.CardIconFont_Large;

        RenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + y), 38, 38);

        font.getData().setScale(0.6f * card.drawScale);
        RenderHelpers.WriteOnCard(sb, card, font, "x" + (int) scaling, (offset_x + 9), (offset_y + y - 12), Settings.CREAM_COLOR.cpy(), true);
        font.getData().setScale(1);

        return 36;
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, float offset_y, float alpha)
    {
        final Vector2 offset = new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y);

        RenderHelpers.DrawOnCardAuto(sb, card, texture, offset, 64, 64, Color.WHITE, alpha);

        return 38;
    }

    protected float UpdateBadgeAlpha()
    {
        if (card.isPreview)
        {
            return card.transparency - badgeAlphaOffset;
        }

        if (card.cardsToPreview instanceof EYBCard)
        {
            ((EYBCard)card.cardsToPreview).cardText.badgeAlphaOffset = badgeAlphaOffset;
        }

        if (card.CanRenderTip() && !card.isPopup)
        {
            if (badgeAlphaOffset < badgeAlphaTargetOffset)
            {
                badgeAlphaOffset += Gdx.graphics.getRawDeltaTime() * 0.33f;
                if (badgeAlphaOffset > badgeAlphaTargetOffset)
                {
                    badgeAlphaOffset = badgeAlphaTargetOffset;
                    badgeAlphaTargetOffset = -0.9f;
                }
            }
            else
            {
                badgeAlphaOffset -= Gdx.graphics.getRawDeltaTime() * 0.5f;
                if (badgeAlphaOffset < badgeAlphaTargetOffset)
                {
                    badgeAlphaOffset = badgeAlphaTargetOffset;
                    badgeAlphaTargetOffset = 0.5f;
                }
            }

            if (card.transparency >= 1 && badgeAlphaOffset > 0)
            {
                return card.transparency - badgeAlphaOffset;
            }
        }
        else
        {
            badgeAlphaOffset = -0.2f;
            badgeAlphaTargetOffset = 0.5f;
        }

        return card.transparency;
    }
}
