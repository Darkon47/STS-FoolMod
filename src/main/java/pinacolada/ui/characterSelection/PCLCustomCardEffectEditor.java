package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardBuilder;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.*;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_EnterStance;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_StackPower;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.ui.controls.GUI_Dropdown;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_SearchableDropdown;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLJUtils;

public class PCLCustomCardEffectEditor extends GUIElement {
    public static final float MENU_WIDTH = Scale(200);
    public static final float MENU_HEIGHT = Scale(40);

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
    protected GUI_Dropdown<PCLCardGroupHelper> Piles;
    protected GUI_SearchableDropdown<PCLPowerHelper> Powers;
    protected GUI_SearchableDropdown<PCLOrbHelper> Orbs;
    protected GUI_SearchableDropdown<PCLStanceHelper> Stances;
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
                , BaseCondition::GetSampleText)
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
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.PrimaryCondition)
                .SetItems(PCLJUtils.Map(BaseCondition.GetEligibleConditions(builder.cardColor, 1), bc -> currentPrimaryCondition != null && bc.effectID.equals(currentPrimaryCondition.effectID) ? currentPrimaryCondition : bc));
        SecondaryConditions = new GUI_Dropdown<BaseCondition>(new AdvancedHitbox(hb.x + MENU_WIDTH, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , BaseCondition::GetSampleText)
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
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.SecondaryCondition)
                .SetItems(PCLJUtils.Map(BaseCondition.GetEligibleConditions(builder.cardColor, 2), bc -> currentSecondaryCondition != null && bc.effectID.equals(currentSecondaryCondition.effectID) ? currentSecondaryCondition : bc));
        Effects = new GUI_Dropdown<BaseEffect>(new AdvancedHitbox(hb.x + MENU_WIDTH * 2, hb.y, MENU_WIDTH, MENU_HEIGHT)
                , BaseEffect::GetSampleText)
                .SetOnChange(effects -> {
                    if (!effects.isEmpty()) {
                        currentEffect = effects.get(0);
                        Affinities.SetActive(currentEffect instanceof BaseEffect_Affinity);
                        Powers.SetActive(currentEffect instanceof BaseEffect_StackPower);
                        Orbs.SetActive(currentEffect instanceof BaseEffect_Orb);
                        Stances.SetActive(currentEffect instanceof BaseEffect_EnterStance);
                        ValueEditor.SetValue(currentEffect.amount, currentEffect.upgrade);
                        Targets.SetActive(!(currentEffect instanceof BaseEffect_CardGroup));
                        Piles.SetActive(currentEffect instanceof BaseEffect_CardGroup);
                    }
                    else {
                        currentEffect = null;
                        Affinities.SetActive(false);
                        Powers.SetActive(false);
                        Targets.SetActive(true);
                        Piles.SetActive(false);
                        ValueEditor.SetValue(0, 0);
                    }
                    ConstructEffect();
                })
                .SetCanAutosizeButton(true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.Effect)
                .SetItems(PCLJUtils.Map(BaseEffect.GetEligibleEffects(builder.cardColor, 3), bc -> currentEffect != null && bc.effectID.equals(currentEffect.effectID) ? currentEffect : bc));
        ValueEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(hb.x + MENU_WIDTH * 3.5f, hb.y, MENU_WIDTH / 5, MENU_HEIGHT)
                , PGR.PCL.Strings.SeriesUI.Amount, (val, upVal) -> {
                    if (currentEffect != null) {
                        currentEffect.SetAmount(val, upVal);
                        ConstructEffect();
                    }
                })
                .SetLimits(-999, 999)
                .SetValue(currentEffect != null ? currentEffect.amount : 0, currentEffect != null ? currentEffect.upgrade : 0);
        Targets = new GUI_Dropdown<PCLCardTarget>(new AdvancedHitbox(hb.x + MENU_WIDTH * 4, hb.y, MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(targets -> {
                    if (currentEffect != null && !targets.isEmpty()) {
                        currentEffect.SetTarget(targets.get(0));
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForOption(PCLCardTarget::GetTitle, false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.CardTarget)
                .SetCanAutosizeButton(true)
                .SetItems(PCLCardTarget.values())
                .SetSelection(currentEffect != null ? currentEffect.target : PCLCardTarget.None, false);
        Piles = new GUI_Dropdown<PCLCardGroupHelper>(new AdvancedHitbox(hb.x + MENU_WIDTH * 4, hb.y, MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(targets -> {
                    if (currentEffect instanceof BaseEffect_CardGroup) {
                        ((BaseEffect_CardGroup) currentEffect).Set(targets);
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForOption(c -> c.Name, false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.CardTarget)
                .SetCanAutosizeButton(true)
                .SetIsMultiSelect(true)
                .SetItems(PCLCardGroupHelper.GetAll());

        Affinities = new GUI_Dropdown<PCLAffinity>(new AdvancedHitbox(hb.x + MENU_WIDTH * 5, hb.y, MENU_WIDTH, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_Affinity) {
                        ((BaseEffect_Affinity) currentEffect).Set(types);
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForOption(item -> {
                    return item.GetTooltip().GetTitleOrIcon();
                }, true)
                .SetIsMultiSelect(true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.SeriesUI.Affinities)
                .SetCanAutosize(false, false)
                .SetItems(PCLAffinity.GetAvailableAffinities(builder.cardColor));
        Powers = (GUI_SearchableDropdown<PCLPowerHelper>) new GUI_SearchableDropdown<PCLPowerHelper>(new AdvancedHitbox(hb.x + MENU_WIDTH * 5, hb.y, MENU_WIDTH * 1.2f, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_StackPower) {
                        ((BaseEffect_StackPower) currentEffect).Set(types);
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForOption(item -> {
                    return item.Tooltip.GetTitleOrIcon() + " " + item.Tooltip.title;
                }, true)
                .SetIsMultiSelect(true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.Powers)
                .SetCanAutosize(false, false)
                .SetItems(PCLPowerHelper.Values());
        Orbs = (GUI_SearchableDropdown<PCLOrbHelper>) new GUI_SearchableDropdown<PCLOrbHelper>(new AdvancedHitbox(hb.x + MENU_WIDTH * 5, hb.y, MENU_WIDTH * 1.2f, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_Orb && types.size() > 0) {
                        ((BaseEffect_Orb) currentEffect).Set(types.get(0));
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForOption(item -> {
                    return item.Tooltip.GetTitleOrIcon() + " " + item.Tooltip.title;
                }, true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.Powers)
                .SetCanAutosize(false, false)
                .SetItems(PCLOrbHelper.Values());
        Stances = (GUI_SearchableDropdown<PCLStanceHelper>) new GUI_SearchableDropdown<PCLStanceHelper>(new AdvancedHitbox(hb.x + MENU_WIDTH * 5, hb.y, MENU_WIDTH * 1.2f, MENU_HEIGHT))
                .SetOnChange(types -> {
                    if (currentEffect instanceof BaseEffect_EnterStance && types.size() > 0) {
                        ((BaseEffect_EnterStance) currentEffect).Set(types.get(0));
                        ConstructEffect();
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    return items.size() > 0 ? items.get(0).Tooltip.title : "";
                }, null, false)
                .SetLabelFunctionForOption(item -> {
                    return item.Tooltip.GetTitleOrIcon() + " " + item.Tooltip.title;
                }, true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.Powers)
                .SetCanAutosize(false, false)
                .SetItems(PCLStanceHelper.Values());

        if (currentEffect instanceof BaseEffect_Affinity) {
            Affinities.SetSelection(((BaseEffect_Affinity) currentEffect).GetAffinities(), false);
        }
        else {
            Affinities.SetActive(false);
        }

        if (currentEffect instanceof BaseEffect_StackPower) {
            Powers.SetSelection(((BaseEffect_StackPower) currentEffect).GetPowers(), false);
        }
        else {
            Powers.SetActive(false);
        }

        if (currentEffect instanceof BaseEffect_Orb) {
            Orbs.SetSelection(((BaseEffect_Orb) currentEffect).GetOrb(), false);
        }
        else {
            Orbs.SetActive(false);
        }
        if (currentEffect instanceof BaseEffect_EnterStance) {
            Stances.SetSelection(((BaseEffect_EnterStance) currentEffect).GetStance(), false);
        }
        else {
            Stances.SetActive(false);
        }

        if (currentEffect instanceof BaseEffect_CardGroup) {
            Piles.SetSelection(((BaseEffect_CardGroup) currentEffect).GetGroups(), false);
            Targets.SetActive(false);
        }
        else {
            Piles.SetActive(false);
        }

        if (currentEffect != null) {
            Effects.SetSelection(currentEffect, false);
        }
        if (currentSecondaryCondition != null) {
            SecondaryConditions.SetSelection(currentSecondaryCondition, false);
        }
        if (currentPrimaryCondition != null) {
            PrimaryConditions.SetSelection(currentPrimaryCondition, false);
        }
    }

    @Override
    public void Update() {
        this.header.TryUpdate();
        this.PrimaryConditions.TryUpdate();
        this.SecondaryConditions.TryUpdate();
        this.Effects.TryUpdate();
        this.Targets.TryUpdate();
        this.Piles.TryUpdate();
        this.Affinities.TryUpdate();
        this.Powers.TryUpdate();
        this.Orbs.TryUpdate();
        this.Stances.TryUpdate();
        this.ValueEditor.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb) {
        this.header.TryRender(sb);
        this.PrimaryConditions.TryRender(sb);
        this.SecondaryConditions.TryRender(sb);
        this.Effects.TryRender(sb);
        this.Targets.TryRender(sb);
        this.Piles.TryRender(sb);
        this.Affinities.TryRender(sb);
        this.Powers.TryRender(sb);
        this.Orbs.TryRender(sb);
        this.Stances.TryRender(sb);
        this.ValueEditor.TryRender(sb);
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
