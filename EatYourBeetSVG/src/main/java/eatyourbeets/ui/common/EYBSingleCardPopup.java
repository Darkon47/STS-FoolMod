package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

// TODO: Improve this
public class EYBSingleCardPopup extends GUIElement
{
    //private static final MethodInfo _canToggleBetaArt = JavaUtilities.GetMethod("canToggleBetaArt", SingleCardViewPopup.class);
    private static final String[] TEXT = SingleCardViewPopup.TEXT;

    private final GUI_Toggle upgradeToggle;
    private final GUI_Toggle betaArtToggle;

    private final Hitbox nextHb;
    private final Hitbox prevHb;
    private final Hitbox cardHb;
    private final Hitbox upgradeHb;
    private final Hitbox betaArtHb;

    private EYBCard card;
    private EYBCard upgradedCard;
    private CardGroup group;
    private AbstractCard prevCard;
    private AbstractCard nextCard;
    private boolean viewBetaArt;
    private float fadeTimer;
    private Color fadeColor;

    public EYBSingleCardPopup()
    {
        this.fadeColor = Color.BLACK.cpy();
        this.upgradeHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
        this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
        this.prevHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.nextHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.cardHb = new Hitbox(550.0F * Settings.scale, 770.0F * Settings.scale);
        this.viewBetaArt = false;
        this.isActive = false;

        upgradeToggle = new GUI_Toggle(upgradeHb).SetText(TEXT[6])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont)
        .SetOnToggle(this::ToggleUpgrade);

        betaArtToggle = new GUI_Toggle(betaArtHb).SetText(TEXT[14])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont)
        .SetOnToggle(this::ToggleBetaArt);
    }

    public void Open(EYBCard card, CardGroup group)
    {
        CardCrawlGame.isPopupOpen = true;

        this.card = card.MakePopupCopy();
        this.upgradedCard = null;
        this.isActive = true;
        this.prevCard = null;
        this.nextCard = null;
        this.group = group;

        if (group != null)
        {
            for (int i = 0; i < group.size(); ++i)
            {
                if (group.group.get(i) == card)
                {
                    if (i != 0)
                    {
                        this.prevCard = group.group.get(i - 1);
                    }

                    if (i != group.size() - 1)
                    {
                        this.nextCard = group.group.get(i + 1);
                    }
                    break;
                }
            }

            this.prevHb.move((float) Settings.WIDTH / 2.0F - 400.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F);
            this.nextHb.move((float) Settings.WIDTH / 2.0F + 400.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F);
        }

        this.cardHb.move((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F);

        this.fadeTimer = 0.25F;
        this.fadeColor.a = 0.0F;

        this.betaArtToggle.SetActive(false);// (boolean)_canToggleBetaArt.Invoke(CardCrawlGame.cardPopup));
        this.upgradeToggle.SetActive(SingleCardViewPopup.enableUpgradeToggle && card.canUpgrade());

        if (betaArtToggle.isActive)
        {
            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);

            if (upgradeToggle.isActive)
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2.0F + 270.0F * Settings.scale, 70.0F * Settings.scale);
                this.upgradeHb.move((float) Settings.WIDTH / 2.0F - 180.0F * Settings.scale, 70.0F * Settings.scale);
            }
            else
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
            }
        }
        else
        {
            this.upgradeHb.move((float) Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
        }
    }

    public EYBCard GetCard()
    {
        if (SingleCardViewPopup.isViewingUpgrade)
        {
            if (upgradedCard == null)
            {
                upgradedCard = card.MakePopupCopy();
                upgradedCard.upgrade();
                upgradedCard.displayUpgrades();
            }

            return upgradedCard;
        }
        else
        {
            return card;
        }
    }

    public void Close()
    {
        if (AbstractDungeon.player != null)
        {
            SingleCardViewPopup.isViewingUpgrade = false;
        }

        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        this.isActive = false;

        if (this.card != null)
        {
            this.card.Dispose();
            this.card = null;
        }

        if (this.upgradedCard != null)
        {
            this.upgradedCard.Dispose();
            this.upgradedCard = null;
        }
    }

    @Override
    public void Update()
    {
        this.cardHb.update();
        this.UpdateArrows();
        this.UpdateInput();

        this.fadeTimer = Math.max(0, fadeTimer - Gdx.graphics.getDeltaTime());
        this.fadeColor.a = Interpolation.pow2In.apply(0.9F, 0.0F, this.fadeTimer * 4.0F);

        this.upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).TryUpdate();
        this.betaArtToggle.SetToggle(viewBetaArt).TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        sb.setColor(this.fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        sb.setColor(Color.WHITE);

        EYBCard card = GetCard();

        card.renderInLibrary(sb);
        card.renderCardTip(sb);

        if (this.prevCard != null)
        {
            RenderArrow(sb, prevHb, CInputActionSet.pageLeftViewDeck, false);
        }

        if (this.nextCard != null)
        {
            RenderArrow(sb, nextHb, CInputActionSet.pageRightViewExhaust, true);
        }

        this.cardHb.render(sb);

        FontHelper.cardTitleFont.getData().setScale(1);
        if (upgradeToggle.isActive)
        {
            upgradeToggle.Render(sb);

            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.upgradeHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }
        }
        if (betaArtToggle.isActive)
        {
            betaArtToggle.Render(sb);

            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.topPanel.getKeyImg(), this.betaArtHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }
        }
    }

    private void ToggleBetaArt(boolean value)
    {
        this.viewBetaArt = value;
        UnlockTracker.betaCardPref.putBoolean(this.card.cardID, this.viewBetaArt);
        UnlockTracker.betaCardPref.flush();
    }

    private void ToggleUpgrade(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    private void UpdateArrows()
    {
        if (this.prevCard != null)
        {
            this.prevHb.update();
            if (this.prevHb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.prevHb.clicked || this.prevCard != null && CInputActionSet.pageLeftViewDeck.isJustPressed())
            {
                this.prevHb.clicked = false;
                CInputActionSet.pageLeftViewDeck.unpress();
                OpenNext(prevCard);
            }
        }

        if (this.nextCard != null)
        {
            this.nextHb.update();
            if (this.nextHb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.nextHb.clicked || this.nextCard != null && CInputActionSet.pageRightViewExhaust.isJustPressed())
            {
                this.nextHb.clicked = false;
                CInputActionSet.pageRightViewExhaust.unpress();
                OpenNext(nextCard);
            }
        }
    }

    private void UpdateInput()
    {
        if (InputHelper.justClickedLeft)
        {
            if (this.prevCard != null && this.prevHb.hovered)
            {
                this.prevHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }

            if (this.nextCard != null && this.nextHb.hovered)
            {
                this.nextHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }
        }

        if (InputHelper.justClickedLeft)
        {
            if (!this.cardHb.hovered && !this.upgradeHb.hovered && (this.betaArtHb == null || !this.betaArtHb.hovered))
            {
                JavaUtilities.Log(this, "Closing");
                Close();
                InputHelper.justClickedLeft = false;
            }
        }
        else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed())
        {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            Close();
        }

        if (this.prevCard != null && InputActionSet.left.isJustPressed())
        {
            OpenNext(prevCard);
        }
        else if (this.nextCard != null && InputActionSet.right.isJustPressed())
        {
            OpenNext(nextCard);
        }
    }

    private void OpenNext(AbstractCard card)
    {
        boolean tmp = SingleCardViewPopup.isViewingUpgrade;
        this.Close();
        CardCrawlGame.cardPopup.open(card, this.group);
        SingleCardViewPopup.isViewingUpgrade = tmp;
        this.fadeTimer = 0.0F;
        this.fadeColor.a = 0.9F;
    }

    private void RenderArrow(SpriteBatch sb, Hitbox hb, CInputAction action, boolean flipX)
    {
        sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128.0F, hb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, flipX, false);
        if (Settings.isControllerMode)
        {
            sb.draw(action.getKeyImg(), hb.cX - 32.0F, hb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        if (hb.hovered)
        {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
            sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128.0F, hb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, flipX, false);
            sb.setColor(Color.WHITE);
            sb.setBlendFunction(770, 771);
        }

        hb.render(sb);
    }
}
