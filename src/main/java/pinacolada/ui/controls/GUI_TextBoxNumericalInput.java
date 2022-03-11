package pinacolada.ui.controls;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.delegates.ActionT1;

public class GUI_TextBoxNumericalInput extends GUI_TextBoxInput {

    protected ActionT1<Integer> onUpdateNumber;
    protected ActionT1<Integer> onCompleteNumber;

    public GUI_TextBoxNumericalInput(Texture backgroundTexture, Hitbox hb) {
        super(backgroundTexture, hb);
    }

    public GUI_TextBoxNumericalInput SetOnCompleteNumber(ActionT1<Integer> onComplete) {
        this.onCompleteNumber = onComplete;
        return this;
    }

    public GUI_TextBoxNumericalInput SetOnUpdateNumber(ActionT1<Integer> onUpdate) {
        this.onUpdateNumber = onUpdate;
        return this;
    }

    @Override
    public boolean acceptCharacter(char c) {
        return label.font.getData().hasGlyph(c) && Character.isDigit(c);
    }

    @Override
    public void End(boolean commit) {
        super.End(commit);
        if (commit && onCompleteNumber != null && !label.text.isEmpty()) {
            onCompleteNumber.Invoke(Integer.parseInt(label.text));
        }
    }
}
