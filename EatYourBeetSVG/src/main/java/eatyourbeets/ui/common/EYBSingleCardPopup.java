package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class EYBSingleCardPopup extends GUIElement
{
    // TODO: This method cannot be found in certain unknown circumstances
    //private static final MethodInfo _canToggleBetaArt = JUtils.GetMethod("canToggleBetaArt", SingleCardViewPopup.class);
    private static final float ICON_SIZE = 64f * Settings.scale;
    private static final String[] TEXT = SingleCardViewPopup.TEXT;

    private final GUI_Toggle upgradeToggle;
    private final GUI_Toggle betaArtToggle;
    private final GUI_Button changeVariant;
    private final GUI_Button changeVariantNext;
    private final GUI_Button changeVariantPrev;
    private final GUI_TextBox changeVariantNumber;
    private final GUI_Label changeVariantLabel;
    private final GUI_Label changeVariantDescription;
    private final GUI_Label maxCopiesLabel;
    private final GUI_Label maxCopiesCount;
    private final GUI_Label maxCopiesDescription;

    private final Hitbox nextHb;
    private final Hitbox prevHb;
    private final Hitbox cardHb;
    private final Hitbox upgradeHb;
    private final Hitbox betaArtHb;
    private final Hitbox changeVariantHb;
    private final Hitbox changeVariantNextHb;
    private final Hitbox changeVariantPrevHb;
    private final Hitbox changeVariantValueHb;

    private final Texture buttonTexture = GR.Common.Images.HexagonalButton.Texture();
    private final Texture buttonBorderTexture = GR.Common.Images.HexagonalButtonBorder.Texture();

    private final AnimatorStrings.SingleCardPopupButtons buttonStrings = GR.Animator.Strings.SingleCardPopupButtons;

    private EYBCard baseCard;
    private EYBCard card;
    private EYBCard upgradedCard;
    private CardGroup group;
    private AbstractCard prevCard;
    private AbstractCard nextCard;
    private boolean viewBetaArt;
    private boolean viewChangeVariant;
    private float fadeTimer;
    private Color fadeColor;
    private int currentForm;

    public EYBSingleCardPopup()
    {
        this.fadeColor = Color.BLACK.cpy();
        this.upgradeHb = new Hitbox(250f * Settings.scale, 80f * Settings.scale);
        this.betaArtHb = new Hitbox(250f * Settings.scale, 80f * Settings.scale);
        this.prevHb = new Hitbox(160f * Settings.scale, 160f * Settings.scale);
        this.nextHb = new Hitbox(160f * Settings.scale, 160f * Settings.scale);
        this.cardHb = new Hitbox(550f * Settings.scale, 770f * Settings.scale);
        this.changeVariantHb = new Hitbox(200f * Settings.scale, 150f * Settings.scale);
        this.changeVariantNextHb = new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 + ICON_SIZE * 3.5f, changeVariantHb.height * 0.8f, false);
        this.changeVariantPrevHb = new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 + ICON_SIZE * 1.5f, changeVariantHb.height * 0.8f, false);
        this.changeVariantValueHb = new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 + ICON_SIZE * 2.5f, changeVariantHb.height * 0.8f, false);
        this.viewBetaArt = false;
        this.isActive = false;
        this.currentForm = 0;

        this.upgradeToggle = new GUI_Toggle(upgradeHb).SetText(TEXT[6])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont, 1)
        .SetOnToggle(this::ToggleUpgrade);

        this.betaArtToggle = new GUI_Toggle(betaArtHb).SetText(TEXT[14])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont, 1)
        .SetOnToggle(this::ToggleBetaArt);

        this.changeVariant = new GUI_Button(buttonTexture, changeVariantHb)
                .SetBorder(buttonBorderTexture, Color.WHITE)
                .SetClickDelay(0.3f)
                .SetDimensions(ScreenW(0.18f), ScreenH(0.07f))
                .SetText(buttonStrings.ChangeVariant)
                .SetOnClick(this::ChangeCardForm)
                .SetColor(Color.FIREBRICK);

        this.changeVariantNext = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, changeVariantNextHb)
                .SetOnClick(() -> ChangePreviewForm(currentForm + 1))
                .SetText(null);

        this.changeVariantPrev = new GUI_Button(ImageMaster.CF_LEFT_ARROW, changeVariantPrevHb)
                .SetOnClick(() -> ChangePreviewForm(currentForm - 1))
                .SetText(null);

        this.changeVariantNumber = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), changeVariantValueHb)
                .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

        this.changeVariantLabel = new GUI_Label(EYBFontHelper.CardDescriptionFont_Large,
                new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 - ICON_SIZE * 2, changeVariantHb.height * 1.6f, false))
                .SetAlignment(0.5f, 0.5f) // 0.1f
                .SetText(buttonStrings.Variant + ":");

        this.changeVariantDescription = new GUI_Label(EYBFontHelper.CardTooltipFont,
                new RelativeHitbox(changeVariantHb, ScreenW(0.21f), ScreenH(0.07f), changeVariantHb.width / 2, -changeVariantHb.height * 0.6f, false))
                .SetAlignment(0.9f, 0.1f, true)
                .SetText(buttonStrings.ChangeVariantTooltipAlways);

        this.maxCopiesLabel = new GUI_Label(EYBFontHelper.CardDescriptionFont_Large,
                new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 - ICON_SIZE * 1.5f, changeVariantHb.height * 4.3f, false))
                .SetAlignment(0.5f, 0.5f);

        this.maxCopiesCount = new GUI_Label(EYBFontHelper.CardTitleFont_Large,
                new RelativeHitbox(changeVariantHb, ICON_SIZE, ICON_SIZE, changeVariantHb.width / 2 + ICON_SIZE * 1.5f, changeVariantHb.height * 4.3f, false))
                .SetColor(new Color(0.7f, 0.9f, 1f, 1f))
                .SetAlignment(0.5f, 0.5f);

        this.maxCopiesDescription = new GUI_Label(EYBFontHelper.CardTooltipFont,
                new RelativeHitbox(changeVariantHb, ScreenW(0.21f), ScreenH(0.07f), changeVariantHb.width / 2, changeVariantHb.height * 3.4f, false))
                .SetAlignment(0.9f, 0.1f, true)
                .SetText(buttonStrings.MaxCopiesTooltip);
    }

    public void Open(EYBCard card, CardGroup group)
    {
        CardCrawlGame.isPopupOpen = true;

        this.baseCard = card;
        this.card = card.MakePopupCopy();
        this.upgradedCard = null;
        this.isActive = true;
        this.prevCard = null;
        this.nextCard = null;
        this.group = group;
        this.currentForm = card.auxiliaryData.form;

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

            this.prevHb.move((float) Settings.WIDTH / 2f - 400f * Settings.scale, (float) Settings.HEIGHT / 2f);
            this.nextHb.move((float) Settings.WIDTH / 2f + 400f * Settings.scale, (float) Settings.HEIGHT / 2f);
        }

        this.cardHb.move((float) Settings.WIDTH / 2f, (float) Settings.HEIGHT / 2f);

        this.fadeTimer = 0.25f;
        this.fadeColor.a = 0f;

        this.betaArtToggle.SetActive(false);// (boolean)_canToggleBetaArt.Invoke(CardCrawlGame.cardPopup));
        this.upgradeToggle.SetActive(SingleCardViewPopup.enableUpgradeToggle && card.canUpgrade());

        if (betaArtToggle.isActive)
        {
            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);

            if (upgradeToggle.isActive)
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2f + 270f * Settings.scale, 70f * Settings.scale);
                this.upgradeHb.move((float) Settings.WIDTH / 2f - 180f * Settings.scale, 70f * Settings.scale);
            }
            else
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2f, 70f * Settings.scale);
            }
        }
        else
        {
            this.upgradeHb.move((float) Settings.WIDTH / 2f, 70f * Settings.scale);
        }

        this.changeVariantHb.move((float) Settings.WIDTH / 2f  - 700f * Settings.scale, Settings.HEIGHT / 2f + 170 * Settings.scale);

        viewChangeVariant = (baseCard != null && baseCard.cardData.CanToggleFromPopup);
        changeVariantDescription.SetText((baseCard != null && !baseCard.cardData.CanToggleFromAlternateForm) ? buttonStrings.ChangeVariantTooltipPermanent : buttonStrings.ChangeVariantTooltipAlways);
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
        this.baseCard = null;
        this.card = null;
        this.upgradedCard = null;
        this.currentForm = 0;
    }

    @Override
    public void Update()
    {
        this.cardHb.update();
        this.changeVariantHb.update();
        this.changeVariantNextHb.update();
        this.changeVariantPrevHb.update();
        this.changeVariantValueHb.update();
        this.UpdateArrows();
        this.UpdateInput();

        this.fadeTimer = Math.max(0, fadeTimer - Gdx.graphics.getDeltaTime());
        this.fadeColor.a = Interpolation.pow2In.apply(0.9f, 0f, this.fadeTimer * 4f);

        this.upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).TryUpdate();
        this.betaArtToggle.SetToggle(viewBetaArt).TryUpdate();

        this.changeVariantNumber.SetText(currentForm);
        this.changeVariantNumber.TryUpdate();
        this.changeVariantPrev.TryUpdate();
        this.changeVariantNext.TryUpdate();
        this.changeVariant.TryUpdate();
        this.changeVariantLabel.TryUpdate();
        this.changeVariantDescription.TryUpdate();
        this.maxCopiesLabel.SetText((AbstractDungeon.player != null ? buttonStrings.CurrentCopies : buttonStrings.MaxCopies) + ":");
        this.maxCopiesLabel.TryUpdate();
        this.maxCopiesCount.SetText(GetCardCopiesText());
        this.maxCopiesCount.TryUpdate();
        this.maxCopiesDescription.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        sb.setColor(this.fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
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
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.upgradeHb.cX - 132f * Settings.scale - 32f, -32f + 67f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
            }
        }
        if (betaArtToggle.isActive)
        {
            betaArtToggle.Render(sb);

            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.topPanel.getKeyImg(), this.betaArtHb.cX - 132f * Settings.scale - 32f, -32f + 67f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
            }
        }

        if (viewChangeVariant && baseCard != null && (baseCard.auxiliaryData.form == 0 || baseCard.cardData.CanToggleFromAlternateForm) && (baseCard.upgraded || SingleCardViewPopup.isViewingUpgrade || baseCard.cardData.UnUpgradedCanToggleForms)) {
            changeVariant.SetInteractable(baseCard.auxiliaryData.form != currentForm);
            changeVariantHb.render(sb);
            changeVariantValueHb.render(sb);
            changeVariantNumber.Render(sb);
            changeVariantLabel.Render(sb);
            if (currentForm > 0) {
                changeVariantPrevHb.render(sb);
                changeVariantPrev.Render(sb);
            }
            if (currentForm < card.cardData.MaxForms - 1) {
                changeVariantNextHb.render(sb);
                changeVariantNext.Render(sb);
            }
            if (baseCard.upgraded || baseCard.cardData.UnUpgradedCanToggleForms) {
                changeVariant.Render(sb);
                changeVariantDescription.Render(sb);
            }
        }

        if (AbstractDungeon.player != null || card.cardData != null) {
            maxCopiesLabel.Render(sb);
            maxCopiesCount.Render(sb);
            maxCopiesDescription.Render(sb);
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
            if (!this.cardHb.hovered && !this.upgradeHb.hovered && (this.betaArtHb == null || !this.betaArtHb.hovered) && (!this.viewChangeVariant || (!this.changeVariantHb.hovered && !this.changeVariantNextHb.hovered && !this.changeVariantPrevHb.hovered && !this.changeVariantValueHb.hovered)))
            {
                JUtils.LogInfo(this, "Closing");
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
        this.fadeTimer = 0f;
        this.fadeColor.a = 0.9f;
    }

    private void RenderArrow(SpriteBatch sb, Hitbox hb, CInputAction action, boolean flipX)
    {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128f, hb.cY - 128f, 128f, 128f, 256f, 256f, Settings.scale, Settings.scale, 0f, 0, 0, 256, 256, flipX, false);
        if (Settings.isControllerMode)
        {
            sb.draw(action.getKeyImg(), hb.cX - 32f, hb.cY - 32f + 100f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
        }

        if (hb.hovered)
        {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1f, 1f, 1f, 0.5f));
            sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128f, hb.cY - 128f, 128f, 128f, 256f, 256f, Settings.scale, Settings.scale, 0f, 0, 0, 256, 256, flipX, false);
            sb.setColor(Color.WHITE);
            sb.setBlendFunction(770, 771);
        }

        hb.render(sb);
    }

    private void ChangeCardForm()
    {
        if (baseCard != null && baseCard.auxiliaryData.form != currentForm) {
            baseCard.SetForm(currentForm, baseCard.timesUpgraded);
            //aCard.canBranch = false;
        }
    }

    public void ChangePreviewForm(int newForm)
    {
        if (card != null) {
            this.currentForm = card.SetForm(newForm, card.timesUpgraded);
            upgradedCard = card.MakePopupCopy();
            upgradedCard.SetForm(newForm, card.timesUpgraded);
            upgradedCard.upgrade();
            upgradedCard.displayUpgrades();
        }

    }

    private String GetCardCopiesText() {
        if (card == null) {
            return "";
        }
        int currentCopies = (AbstractDungeon.player != null ? JUtils.Count(AbstractDungeon.player.masterDeck.group, c -> c.cardID.equals(card.cardID)) : -1);
        int maxCopies = card.cardData != null ? card.cardData.MaxCopies : 0;

        if (currentCopies >= 0 && maxCopies > 0) {
            return currentCopies + "/" + maxCopies;
        }
        else if (currentCopies >= 0) {
            return String.valueOf(currentCopies);
        }
        else {
            return String.valueOf(maxCopies);
        }
    }

}
