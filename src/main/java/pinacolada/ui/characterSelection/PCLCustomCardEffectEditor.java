package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardBuilder;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.BaseEffect_Affinity;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_StackPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Dropdown;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_SearchableDropdown;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.ui.characterSelection.PCLCustomCardPage.MENU_HEIGHT;
import static pinacolada.ui.characterSelection.PCLCustomCardPage.MENU_WIDTH;

public class PCLCustomCardEffectEditor extends GUIElement {

    protected final PCLCardBuilder builder;
    protected Hitbox hb;
    protected ActionT1<BaseEffect> onUpdate;
    protected GUI_Label header;
    protected BaseCondition currentPrimaryCondition;
    protected BaseCondition currentSecondaryCondition;
    protected BaseEffect currentEffect;
    protected BaseEffect.PCLCardValueSource currentEffectSource = BaseEffect.PCLCardValueSource.MagicNumber;
    protected BaseEffect finalEffect;


    protected GUI_Dropdown<BaseCondition> PrimaryConditions;
    protected GUI_Dropdown<BaseCondition> SecondaryConditions;
    protected GUI_Dropdown<BaseEffect> Effects;
    protected GUI_Dropdown<PCLAffinity> Affinities;
    protected GUI_Dropdown<PCLCardTarget> Targets;
    protected GUI_SearchableDropdown<PCLPowerHelper> Powers;
    protected PCLCustomCardValueEditor ValueEditor;



    public PCLCustomCardEffectEditor(PCLCardBuilder builder, BaseEffect effect, Hitbox hb, String title, ActionT1<BaseEffect> onUpdate) {
        this.builder = builder;
        // Initialize the list of effects to that of the current card
        if (effect != null) {
            if (BaseEffect.GetData(effect.effectID).Priority == 1) {
                currentPrimaryCondition = (BaseCondition) effect.MakeCopy();
                BaseEffect child = currentPrimaryCondition.GetChild();
                if (child instanceof BaseCondition) {
                    currentSecondaryCondition = (BaseCondition) child.MakeCopy();
                    child = currentSecondaryCondition.GetChild();
                    if (child != null) {
                        currentEffect = child.MakeCopy();
                    }
                }
                else if (child != null) {
                    currentEffect = child.MakeCopy();
                }
            }
            else if (BaseEffect.GetData(effect.effectID).Priority == 2) {
                currentSecondaryCondition = (BaseCondition) effect.MakeCopy();
                BaseEffect child = currentSecondaryCondition.GetChild();
                if (child != null) {
                    currentEffect = child.MakeCopy();
                }
            }
            else {
                currentEffect = effect.MakeCopy();
            }
        }

        this.hb = hb;
        final float w = hb.width;
        final float h = hb.height;

        this.header = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(hb.x, hb.y + hb.height * 1.6f, w, h))
                .SetAlignment(0.5f,0.0f,false)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f).SetColor(Settings.GOLD_COLOR)
                .SetText(title);

        this.onUpdate = onUpdate;

        PrimaryConditions = new GUI_Dropdown<BaseCondition>(new AdvancedHitbox(hb.x, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , BaseCondition::GetText)
                .SetOnChange(conditions -> {
                    if (!conditions.isEmpty()) {
                        currentPrimaryCondition = conditions.get(0);
                    }
                    else {
                        currentPrimaryCondition = null;
                    }
                    ConstructEffect();
                })
                .SetCanAutosizeButton(true)
                .SetLabelFunctionForButton(null, null,true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.PrimaryCondition)
                .SetItems(PCLJUtils.Map(BaseCondition.GetEligibleConditions(builder.cardColor, 1), bc -> currentPrimaryCondition != null && bc.effectID.equals(currentPrimaryCondition.effectID) ? currentPrimaryCondition : bc));
        SecondaryConditions = new GUI_Dropdown<BaseCondition>(new AdvancedHitbox(hb.x + MENU_WIDTH, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , BaseCondition::GetText)
                .SetOnChange(conditions -> {
                    if (!conditions.isEmpty()) {
                        currentSecondaryCondition = conditions.get(0);
                    }
                    else {
                        currentSecondaryCondition = null;
                    }
                    ConstructEffect();
                })
                .SetCanAutosizeButton(true)
                .SetLabelFunctionForButton(null, null,true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.SecondaryCondition)
                .SetItems(PCLJUtils.Map(BaseCondition.GetEligibleConditions(builder.cardColor, 2), bc -> currentSecondaryCondition != null && bc.effectID.equals(currentSecondaryCondition.effectID) ? currentSecondaryCondition : bc));
        Effects = new GUI_Dropdown<BaseEffect>(new AdvancedHitbox(hb.x + MENU_WIDTH * 2, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , BaseEffect::GetText)
                .SetOnChange(effects -> {
                    if (!effects.isEmpty()) {
                        currentEffect = effects.get(0);
                        Affinities.SetActive(currentEffect instanceof BaseEffect_Affinity);
                        Powers.SetActive(currentEffect instanceof BaseEffect_StackPower);
                        ValueEditor.SetValue(currentEffect.amount, currentEffect.upgrade);
                    }
                    else {
                        currentEffect = null;
                        Affinities.SetActive(false);
                        Powers.SetActive(false);
                        ValueEditor.SetValue(0, 0);
                    }
                    ConstructEffect();
                })
                .SetCanAutosizeButton(true)
                .SetLabelFunctionForButton(null, null,true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.SecondaryCondition)
                .SetItems(PCLJUtils.Map(BaseEffect.GetEligibleEffects(builder.cardColor, 3), bc -> currentEffect != null && bc.effectID.equals(currentEffect.effectID) ? currentEffect : bc));

        Affinities = new GUI_Dropdown<PCLAffinity>(new AdvancedHitbox(hb.x + MENU_WIDTH * 3, hb.y, MENU_WIDTH, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_Affinity) {
                        ((BaseEffect_Affinity) currentEffect).Set(types);
                    }
                })
                .SetLabelFunctionForOption(item -> {
                    return item.GetTooltip().GetTitleOrIcon();
                }, true)
                .SetIsMultiSelect(true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[1])
                .SetCanAutosizeButton(true);
        Powers = (GUI_SearchableDropdown<PCLPowerHelper>) new GUI_SearchableDropdown<PCLPowerHelper>(new AdvancedHitbox(hb.x + MENU_WIDTH * 3, hb.y, MENU_WIDTH, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_StackPower) {
                        ((BaseEffect_StackPower) currentEffect).Set(types);
                    }
                })
                .SetLabelFunctionForOption(item -> {
                    return item.Tooltip.GetTitleOrIcon() + " " + item.Tooltip.title;
                }, true)
                .SetIsMultiSelect(true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[1])
                .SetCanAutosizeButton(true);
        ValueEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(hb.x + MENU_WIDTH * 4, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , CardLibSortHeader.TEXT[3], (val, upVal) -> {
                    if (currentEffect != null) {
                        currentEffect.SetAmount(val, upVal);
                    }
                })
                .SetLimits(-999, 999)
                .SetValue(currentEffect != null ? currentEffect.amount : 0, currentEffect != null ? currentEffect.upgrade : 0);

        if (currentEffect instanceof BaseEffect_Affinity) {
            Affinities.SetItems(((BaseEffect_Affinity) currentEffect).GetAffinities());
        }
        else if (currentEffect instanceof BaseEffect_StackPower) {
            Powers.SetItems(((BaseEffect_StackPower) currentEffect).GetPowers());
        }

        Affinities.SetActive(false);
        Powers.SetActive(false);
    }

    @Override
    public void Update() {
        this.header.TryUpdate();
        this.PrimaryConditions.TryUpdate();
        this.SecondaryConditions.TryUpdate();
        this.Effects.TryUpdate();
        this.Affinities.TryUpdate();
        this.Powers.TryUpdate();
        this.ValueEditor.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb) {
        this.header.TryRender(sb);
        this.PrimaryConditions.TryRender(sb);
        this.SecondaryConditions.TryRender(sb);
        this.Effects.TryRender(sb);
        this.Affinities.TryRender(sb);
        this.Powers.TryRender(sb);
    }

    protected void ConstructEffect() {
        finalEffect = currentEffect != null ? currentEffect.MakeCopy() : null;
        if (currentSecondaryCondition != null) {
            finalEffect = ((BaseCondition) currentSecondaryCondition.MakeCopy())
                    .SetChildEffect(finalEffect);
        }
        if (currentPrimaryCondition != null) {
            finalEffect = ((BaseCondition) currentPrimaryCondition.MakeCopy())
                    .SetChildEffect(finalEffect);
        }
        if (onUpdate != null) {
            onUpdate.Invoke(finalEffect);
        }
    }
}
