package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.Mathf;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.interfaces.delegates.ActionT4;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_TextBoxNumericalInput;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;

import static pinacolada.cards.base.PCLAffinity.MAX_LEVEL;

public class PCLCustomCardAffinityValueEditor extends GUIElement
{
    protected static final float ICON_SIZE = 36f * Settings.scale;

    protected ActionT4<PCLAffinity, Integer, Integer, Integer> onUpdate;
    protected PCLAffinity affinity;
    protected int value;
    protected int value2;
    protected int value3;
    protected GUI_TextBoxNumericalInput displayValue;
    protected GUI_TextBoxNumericalInput displayValue2;
    protected GUI_TextBoxNumericalInput displayValue3;
    protected Hitbox hb;
    protected GUI_Button decrease_button;
    protected GUI_Button decrease_button2;
    protected GUI_Button decrease_button3;
    protected GUI_Button increase_button;
    protected GUI_Button increase_button2;
    protected GUI_Button increase_button3;
    protected GUI_Image affinity_image;
    protected int minimum = 0;
    protected int maximum = 999;

    public PCLCustomCardAffinityValueEditor(Hitbox hb, PCLAffinity affinity, ActionT4<PCLAffinity, Integer, Integer, Integer> onUpdate)
    {
        this.hb = hb;
        this.affinity = affinity;

        final float w = hb.width;
        final float h = hb.height;

        decrease_button = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * -0.4f, h * 0.5f, false))
                .SetOnClick(this::DecreasePrimary)
                .SetText(null);

        decrease_button2 = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * -0.4f, -h * 0.5f, false))
                .SetOnClick(this::DecreaseSecondary)
                .SetText(null);

        decrease_button3 = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, ICON_SIZE * -0.4f, -h * 1.5f, false))
                .SetOnClick(this::DecreaseTertiary)
                .SetText(null);

        increase_button = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.4f), h * 0.5f, false))
                .SetOnClick(this::IncreasePrimary)
                .SetText(null);

        increase_button2 = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.4f), -h * 0.5f, false))
                .SetOnClick(this::IncreaseSecondary)
                .SetText(null);

        increase_button3 = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, w + (ICON_SIZE * 0.4f), -h * 1.5f, false))
                .SetOnClick(this::IncreaseTertiary)
                .SetText(null);

        displayValue = (GUI_TextBoxNumericalInput) new GUI_TextBoxNumericalInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), hb)
                .SetOnCompleteNumber(this::SetValue)
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.6f);

        displayValue2 = (GUI_TextBoxNumericalInput) new GUI_TextBoxNumericalInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(),
                new RelativeHitbox(hb, hb.width, hb.height, w * 0.5f, -h * 0.5f, false))
                .SetOnCompleteNumber(this::SetSecondaryValue)
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.6f);

        displayValue3 = (GUI_TextBoxNumericalInput) new GUI_TextBoxNumericalInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(),
                new RelativeHitbox(hb, hb.width, hb.height, w * 0.5f, -h * 1.5f, false))
                .SetOnCompleteNumber(this::SetTertiaryValue)
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.6f);

        this.affinity_image = new GUI_Image(affinity.GetIcon(), new AdvancedHitbox(hb.x, hb.y + hb.height * 0.8f, ICON_SIZE, ICON_SIZE));

        this.onUpdate = onUpdate;
    }


    @Override
    public void Update() {
        this.hb.update();
        decrease_button.SetInteractable(value > 0).TryUpdate();
        decrease_button2.SetInteractable(value + value2 > 0).TryUpdate();
        decrease_button3.SetInteractable(value3 > minimum).TryUpdate();
        increase_button.SetInteractable(value < MAX_LEVEL).TryUpdate();
        increase_button2.SetInteractable(value + value2 < MAX_LEVEL).TryUpdate();
        increase_button3.SetInteractable(value3 < maximum).TryUpdate();
        displayValue.TryUpdate();
        displayValue2.TryUpdate();
        displayValue3.TryUpdate();
        affinity_image.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        this.hb.render(sb);
        decrease_button3.TryRender(sb);
        increase_button3.TryRender(sb);
        decrease_button2.TryRender(sb);
        increase_button2.TryRender(sb);
        decrease_button.TryRender(sb);
        increase_button.TryRender(sb);
        displayValue3.TryRender(sb);
        displayValue2.TryRender(sb);
        displayValue.TryRender(sb);
        affinity_image.TryRender(sb);
    }

    public void DecreasePrimary()
    {
        SetValue(value - 1, value2, value3);
    }

    public void IncreasePrimary()
    {
        SetValue(value + 1, value2, value3);
    }

    public void DecreaseSecondary()
    {
        SetValue(value, value2 - 1, value3);
    }

    public void IncreaseSecondary()
    {
        SetValue(value, value2 + 1, value3);
    }

    public void DecreaseTertiary()
    {
        SetValue(value, value2, value3 - 1);
    }

    public void IncreaseTertiary()
    {
        SetValue(value, value2, value3 + 1);
    }

    public PCLCustomCardAffinityValueEditor ShowThirdValue(boolean val)
    {
        displayValue3.SetActive(val);
        increase_button3.SetActive(val);
        decrease_button3.SetActive(val);
        return this;
    }

    public PCLCustomCardAffinityValueEditor SetValue(int value)
    {
        return SetValue(value, value2, value3);
    }

    public PCLCustomCardAffinityValueEditor SetValue(int value, int valueSecondary, int valueTertiary)
    {
        this.value = Mathf.Clamp(value, 0, MAX_LEVEL);
        displayValue.SetText(value);
        this.value2 = Mathf.Clamp(valueSecondary, -this.value, MAX_LEVEL - this.value);
        displayValue2.SetText(valueSecondary);
        this.value3 = Mathf.Clamp(valueTertiary, minimum, maximum);
        displayValue3.SetText(valueTertiary);
        onUpdate.Invoke(affinity, value, valueSecondary, valueTertiary);

        return this;
    }

    public PCLCustomCardAffinityValueEditor SetSecondaryValue(int valueSecondary)
    {
        return SetValue(value, valueSecondary, value3);
    }

    public PCLCustomCardAffinityValueEditor SetTertiaryValue(int valueTertiary)
    {
        return SetValue(value, value2, valueTertiary);
    }

    public PCLCustomCardAffinityValueEditor SetTertiaryLimits(int minimum, int maximum)
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
