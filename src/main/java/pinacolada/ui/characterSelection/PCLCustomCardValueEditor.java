package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.Mathf;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_TextBoxNumericalInput;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;

public class PCLCustomCardValueEditor extends GUIElement
{
    protected static final float ICON_SIZE = 36f * Settings.scale;

    protected ActionT2<Integer, Integer> onUpdate;
    protected int value;
    protected int valueSecondary;
    protected GUI_TextBoxNumericalInput displayValue;
    protected GUI_TextBoxNumericalInput displayValueSecondary;
    protected Hitbox hb;
    protected GUI_Button decrease_button;
    protected GUI_Button decrease_button2;
    protected GUI_Button increase_button;
    protected GUI_Button increase_button2;
    protected GUI_Label header;
    protected int minimum = 0;
    protected int maximum = 999;

    public PCLCustomCardValueEditor(Hitbox hb, String title, ActionT2<Integer, Integer> onUpdate)
    {
        this.hb = hb;

        final float w = hb.width;
        final float h = hb.height;

        decrease_button = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * -0.4f, h * 0.5f, false))
                .SetOnClick(this::DecreasePrimary)
                .SetText(null);

        decrease_button2 = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * -0.4f, -h * 0.5f, false))
                .SetOnClick(this::DecreaseSecondary)
                .SetText(null);

        increase_button = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.4f), h * 0.5f, false))
                .SetOnClick(this::IncreasePrimary)
                .SetText(null);

        increase_button2 = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.4f), -h * 0.5f, false))
                .SetOnClick(this::IncreaseSecondary)
                .SetText(null);

        displayValue = (GUI_TextBoxNumericalInput) new GUI_TextBoxNumericalInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), hb)
                .SetOnCompleteNumber(this::SetValue)
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.6f);

        displayValueSecondary = (GUI_TextBoxNumericalInput) new GUI_TextBoxNumericalInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(),
                new RelativeHitbox(hb, hb.width, hb.height, w * 0.5f, -h * 0.5f, false))
                .SetOnCompleteNumber(this::SetSecondaryValue)
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.6f);

        this.header = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(hb.x, hb.y + hb.height * 0.8f, w, h))
                .SetAlignment(0.5f,0.0f,false)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f).SetColor(Settings.GOLD_COLOR)
                .SetText(title);

        this.onUpdate = onUpdate;
    }

    public PCLCustomCardValueEditor SetHeader(BitmapFont font, float fontScale, Color textColor, String text) {
        return SetHeader(font,fontScale,textColor,text,false);
    }

    public PCLCustomCardValueEditor SetHeader(BitmapFont font, float fontScale, Color textColor, String text, boolean smartText) {
        this.header.SetFont(font, fontScale).SetColor(textColor).SetText(text).SetSmartText(smartText).SetActive(true);

        return this;
    }

    public PCLCustomCardValueEditor SetHeader(float x, float y, BitmapFont font, float fontScale, Color textColor, String text, boolean smartText) {
        this.header.SetPosition(x, y)
                .SetFont(font, fontScale).SetColor(textColor).SetText(text).SetSmartText(smartText).SetActive(true);

        return this;
    }


    @Override
    public void Update() {
        this.hb.update();
        decrease_button.SetInteractable(value > minimum).TryUpdate();
        decrease_button2.SetInteractable(value + valueSecondary > minimum).TryUpdate();
        increase_button.SetInteractable(value < maximum).TryUpdate();
        increase_button2.SetInteractable(value + valueSecondary < maximum).TryUpdate();
        displayValue.TryUpdate();
        displayValueSecondary.TryUpdate();
        header.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        this.hb.render(sb);
        decrease_button2.TryRender(sb);
        increase_button2.TryRender(sb);
        decrease_button.TryRender(sb);
        increase_button.TryRender(sb);
        displayValueSecondary.TryRender(sb);
        displayValue.TryRender(sb);
        header.TryRender(sb);
    }

    public void DecreasePrimary()
    {
        SetValue(value - 1, valueSecondary);
    }

    public void IncreasePrimary()
    {
        SetValue(value + 1, valueSecondary);
    }

    public void DecreaseSecondary()
    {
        SetValue(value, valueSecondary - 1);
    }

    public void IncreaseSecondary()
    {
        SetValue(value, valueSecondary + 1);
    }

    public PCLCustomCardValueEditor SetValue(int value)
    {
        return SetValue(value, valueSecondary);
    }

    public PCLCustomCardValueEditor SetValue(int value, int valueSecondary)
    {
        this.value = Mathf.Clamp(value, minimum, maximum);
        displayValue.SetText(value);
        this.valueSecondary = Mathf.Clamp(valueSecondary, minimum - this.value, maximum - this.value);
        displayValueSecondary.SetText(valueSecondary);
        onUpdate.Invoke(value, valueSecondary);

        return this;
    }

    public PCLCustomCardValueEditor SetSecondaryValue(int valueSecondary)
    {
        return SetValue(value, valueSecondary);
    }

    public PCLCustomCardValueEditor SetLimits(int minimum, int maximum)
    {
        if (minimum <= maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        return this;
    }

    public int GetValue() {
        return value;
    }
}
