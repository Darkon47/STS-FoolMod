package pinacolada.ui.controls;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.resources.PGR;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.List;

public class GUI_SearchableDropdown<T> extends GUI_Dropdown<T>
{
    public ArrayList<DropdownRow<T>> originalRows;
    protected GUI_TextInput searchInput;

    public GUI_SearchableDropdown(AdvancedHitbox hb) {
        super(hb);
        Initialize();
    }

    public GUI_SearchableDropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction) {
        super(hb, labelFunction);
        Initialize();
    }

    public GUI_SearchableDropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction, ArrayList<T> options) {
        super(hb, labelFunction, options);
        Initialize();
    }

    public GUI_SearchableDropdown(AdvancedHitbox hb, FuncT1<String, T> labelFunction, ArrayList<T> options, BitmapFont font, int maxRows, boolean canAutosizeButton) {
        super(hb, labelFunction, options, font, maxRows, canAutosizeButton);
        Initialize();
    }

    public GUI_SearchableDropdown<T> SetCanAutosizeButton(boolean value) {
        super.SetCanAutosizeButton(value);
        return this;
    }

    public GUI_SearchableDropdown<T> SetOnChange(ActionT1<List<T>> onChange) {
        super.SetOnChange(onChange);
        return this;
    }

    public GUI_SearchableDropdown<T> SetOnOpenOrClose(ActionT1<Boolean> onOpenOrClose) {
        super.SetOnOpenOrClose(onOpenOrClose);
        return this;
    }

    public GUI_SearchableDropdown<T> SetPosition(float x, float y) {
        super.SetPosition(x, y);
        return this;
    }

    public void OpenOrCloseMenu() {
        super.OpenOrCloseMenu();
        button.showText = !this.isOpen;
        this.rows = this.originalRows;
        searchInput.text = "";
        if (this.isOpen) {
            searchInput.Start();
        }
        else {
            searchInput.End();
        }
    }

    @Override
    public void Update() {
        super.Update();
        this.searchInput.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb) {
        super.Render(sb);
        this.searchInput.TryRender(sb);
    }

    @Override
    public ArrayList<T> GetAllItems() {
        return PCLJUtils.Map(this.originalRows, row -> row.item);
    }

    @Override
    public ArrayList<T> GetCurrentItems() {
        ArrayList<T> items = new ArrayList<>();
        for (Integer i : currentIndices) {
            items.add(this.originalRows.get(i).item);
        }
        return items;
    }

    @Override
    public void updateForSelection(boolean shouldInvoke) {
        int temp = currentIndices.size() > 0 ? currentIndices.first() : 0;
        if (isMultiSelect) {
            this.button.text = labelFunctionButton != null ? labelFunctionButton.Invoke(GetCurrentItems()) : currentIndices.size() + " " + PGR.PCL.Strings.SeriesUI.ItemsSelected;
        }
        else if (currentIndices.size() > 0) {
            this.topVisibleRowIndex = Math.min(temp, this.originalRows.size() - this.visibleRowCount());
            this.button.text = labelFunctionButton != null ? labelFunctionButton.Invoke(GetCurrentItems()) : originalRows.get(temp).label.text;
            if (colorFunctionButton != null) {
                this.button.SetTextColor(colorFunctionButton.Invoke(GetCurrentItems()));
            }

            this.scrollBar.Scroll(this.scrollPercentForTopVisibleRowIndex(this.topVisibleRowIndex), false);
        }
        if (shouldInvoke && onChange != null) {
            onChange.Invoke(GetCurrentItems());
        }
    }

    protected void Initialize() {
        this.originalRows = this.rows;
        searchInput = (GUI_TextInput) new GUI_TextInput(button.font, new RelativeHitbox(button.hb, button.hb.width, button.hb.height, button.hb.width / 2f, button.hb.height / 4f, false))
                .SetOnUpdate(this::OnUpdate)
                .SetText("");
    }

    protected void OnUpdate(String searchInput) {
        if (searchInput == null || searchInput.isEmpty()) {
            this.rows = this.originalRows;
        }
        else {
            this.rows = PCLJUtils.Filter(this.originalRows, row -> row.GetText() != null && row.GetText().toLowerCase().contains(searchInput.toLowerCase()));
            this.topVisibleRowIndex = 0;
        }
    }

}
