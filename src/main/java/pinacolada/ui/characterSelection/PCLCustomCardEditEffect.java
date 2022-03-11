package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLCardBuilder;
import pinacolada.cards.base.PCLCard_Dynamic;
import pinacolada.cards.base.PCLCustomCardSlot;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Toggle;

public class PCLCustomCardEditEffect extends EYBEffectWithCallback<Object> {

    protected static final float CARD_X = Settings.WIDTH * 0.13f;
    protected static final float CARD_Y = Settings.HEIGHT * 0.75f;

    protected ActionT0 OnRemove;
    protected ActionT0 OnSave;
    public PCLCardBuilder TempBuilder;
    protected PCLCustomCardSlot CurrentSlot;
    protected PCLCard_Dynamic PreviewCard;
    protected PCLCustomCardPage Page;
    protected GUI_Toggle upgradeToggle;

    public PCLCustomCardEditEffect(PCLCustomCardSlot slot) {
        CurrentSlot = slot;
        TempBuilder = new PCLCardBuilder(CurrentSlot.Builder);
        Page = new PCLCustomCardPage(this);

        upgradeToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(PGR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.15f, Settings.HEIGHT * 0.45f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
                .SetText(SingleCardViewPopup.TEXT[6])
                .SetOnToggle(PCLCustomCardEditEffect::ToggleViewUpgrades);
        RefreshCard();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        Page.TryUpdate();
        upgradeToggle.TryUpdate();
        PreviewCard.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        Page.Render(sb);
        upgradeToggle.TryRender(sb);
        if (SingleCardViewPopup.isViewingUpgrade) {
            PreviewCard.renderUpgradePreview(sb);
        }
        else {
            PreviewCard.render(sb);
        }
    }

    public void RefreshCard() {
        PreviewCard = TempBuilder.Build();
        PreviewCard.isPopup = true;
        PreviewCard.isPreview = true;
        PreviewCard.drawScale = PreviewCard.targetDrawScale = 1f;
        PreviewCard.current_x = PreviewCard.target_x = CARD_X;
        PreviewCard.current_y = PreviewCard.target_y = CARD_Y;
    }

    protected void End() {
        Complete();
    }

    protected void Save() {
        CurrentSlot.Builder = TempBuilder;
        if (this.OnSave != null) {
            this.OnSave.Invoke();
        }
        End();
    }

    protected void Remove() {
        if (this.OnRemove != null) {
            this.OnRemove.Invoke();
        }
    }

    public PCLCustomCardEditEffect SetActions(ActionT0 onRemove, ActionT0 onSave)
    {
        this.OnRemove = onRemove;
        this.OnSave = onSave;

        return this;
    }

    private static void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

}
