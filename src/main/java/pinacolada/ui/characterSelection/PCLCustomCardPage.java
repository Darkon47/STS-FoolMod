package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.leaderboards.LeaderboardScreen;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.*;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static pinacolada.ui.AbstractScreen.CreateHexagonalButton;

public class PCLCustomCardPage extends GUIElement {
    public static final int EFFECT_COUNT = 2;

    public static final float MENU_WIDTH = Scale(160);
    public static final float MENU_HEIGHT = Scale(40);
    public static final float SPACING_WIDTH = ScreenW(0.06f);
    protected static final float START_X = ScreenW(0.25f);
    protected static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    protected static final float PAD_Y = Scale(10);

    protected PCLCustomCardEditEffect effect;
    protected GUI_TextBoxInput NameInput;
    protected GUI_Dropdown<PCLCardTagHelper> TagsDropdown;
    protected GUI_Dropdown<PCLCardTarget> TargetDropdown;
    protected GUI_Dropdown<AbstractCard.CardRarity> RaritiesDropdown;
    protected GUI_Dropdown<AbstractCard.CardType> TypesDropdown;
    protected GUI_Dropdown<PCLAttackType> AttackTypeDropdown;
    protected GUI_Dropdown<AbstractGameAction.AttackEffect> AttackEffectDropdown;
    protected GUI_Dropdown<CardSeries> SeriesDropdown;
    protected PCLCustomCardValueEditor CostEditor;
    protected PCLCustomCardValueEditor DamageEditor;
    protected PCLCustomCardValueEditor BlockEditor;
    protected PCLCustomCardValueEditor MagicNumberEditor;
    protected PCLCustomCardValueEditor SecondaryValueEditor;
    protected PCLCustomCardValueEditor HitCountEditor;
    protected ArrayList<PCLCustomCardAffinityValueEditor> AffinityEditors = new ArrayList<>();
    protected ArrayList<PCLCustomCardEffectEditor> EffectEditors = new ArrayList<>();
    protected GUI_Button cancel_button;
    protected GUI_Button save_button;
    protected GUI_Label upgrade_label;
    protected GUI_Label upgrade_label2;
    protected GUI_Label scaling_label;
    protected GUI_Toggle tags_toggle;

    protected ArrayList<BaseEffect> CurrentEffects = new ArrayList<>();

    public PCLCustomCardPage(PCLCustomCardEditEffect effect) {
        final float buttonHeight = ScreenH(0.07f);
        final float labelHeight = ScreenH(0.04f);
        final float buttonWidth = ScreenW(0.18f);
        final float labelWidth = ScreenW(0.20f);
        final float button_cY = buttonHeight * 1.5f;
        this.effect = effect;
        CurrentEffects.addAll(effect.TempBuilder.effects);
        while (CurrentEffects.size() < EFFECT_COUNT) {
            CurrentEffects.add(null);
        }

        NameInput = (GUI_TextBoxInput) new GUI_TextBoxInput(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(),
                new AdvancedHitbox(START_X, ScreenH(0.87f), MENU_WIDTH * 1.8f, MENU_HEIGHT * 1.6f))
                .SetOnComplete(s -> {
                    effect.TempBuilder.SetName(s);
                    effect.RefreshCard();
                })
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.95f, Settings.GOLD_COLOR, LeaderboardScreen.TEXT[7])
                .SetBackgroundTexture(PGR.PCL.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.1f)
                .SetFont(FontHelper.cardTitleFont, 0.7f)
                .SetText(effect.TempBuilder.cardStrings.NAME);
        RaritiesDropdown = new GUI_Dropdown<AbstractCard.CardRarity>(new AdvancedHitbox(ScreenW(0.43f), ScreenH(0.88f), MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(rarities -> {
                    if (!rarities.isEmpty()) {
                        effect.TempBuilder.SetRarity(rarities.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    return StringUtils.join(PCLJUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[0])
                .SetItems(AbstractCard.CardRarity.values())
                .SetSelection(effect.TempBuilder.cardRarity, false);
        TypesDropdown = new GUI_Dropdown<AbstractCard.CardType>(new AdvancedHitbox(ScreenW(0.53f), ScreenH(0.88f), MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(types -> {
                    if (!types.isEmpty()) {
                        effect.TempBuilder.SetType(types.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    return StringUtils.join(PCLJUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, CardLibSortHeader.TEXT[1])
                .SetCanAutosizeButton(true)
                .SetItems(Arrays.stream(AbstractCard.CardType.values()).sorted().collect(Collectors.toList()))
                .SetSelection(effect.TempBuilder.cardType, false);
        TargetDropdown = new GUI_Dropdown<PCLCardTarget>(new AdvancedHitbox(ScreenW(0.63f), ScreenH(0.88f), MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(targets -> {
                    if (!targets.isEmpty()) {
                        effect.TempBuilder.SetCardTarget(targets.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetLabelFunctionForOption(PCLCardTarget::GetTitle, false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.CardTarget)
                .SetCanAutosizeButton(true)
                .SetItems(PCLCardTarget.values())
                .SetSelection(effect.TempBuilder.attackTarget, false);
        SeriesDropdown = (GUI_SearchableDropdown<CardSeries>) new GUI_SearchableDropdown<CardSeries>(new AdvancedHitbox(ScreenW(0.77f), ScreenH(0.88f), MENU_WIDTH, MENU_HEIGHT), cs -> cs.LocalizedName)
                .SetOnChange(selectedSeries -> {
                    if (!selectedSeries.isEmpty()) {
                        effect.TempBuilder.SetSeries(selectedSeries.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.SeriesUI.SeriesUI)
                .SetCanAutosizeButton(true)
                .SetItems(CardSeries.GetAllSeries(true))
                .SetSelection(effect.TempBuilder.series, false);

        // Number editors

        float curW = START_X;
        upgrade_label = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(curW, ScreenH(0.77f) - MENU_HEIGHT, MENU_WIDTH / 4, MENU_HEIGHT))
                .SetAlignment(0.5f,0.0f,false)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.6f).SetColor(Settings.BLUE_TEXT_COLOR)
                .SetText(PGR.PCL.Strings.CardEditor.Upgrades);
        curW += SPACING_WIDTH;
        CostEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , CardLibSortHeader.TEXT[3], (val, upVal) -> {
                    effect.TempBuilder.SetCost(val, upVal);
                    effect.RefreshCard();
                })
                .SetLimits(-2, 999)
                .SetValue(effect.TempBuilder.cost, effect.TempBuilder.costUpgrade);
        curW += SPACING_WIDTH;
        DamageEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , PGR.PCL.Strings.CardEditor.Damage, (val, upVal) -> {
            effect.TempBuilder.SetDamage(val, upVal);
            effect.RefreshCard();
        })
                .SetLimits(0, 999)
                .SetValue(effect.TempBuilder.damage, effect.TempBuilder.damageUpgrade);
        curW += SPACING_WIDTH;
        BlockEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , PGR.PCL.Strings.CardEditor.Block, (val, upVal) -> {
            effect.TempBuilder.SetBlock(val, upVal);
            effect.RefreshCard();
        })
                .SetLimits(0, 999)
                .SetValue(effect.TempBuilder.block, effect.TempBuilder.blockUpgrade);
        curW += SPACING_WIDTH;
        MagicNumberEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , PGR.PCL.Strings.CardEditor.MagicNumber, (val, upVal) -> {
            effect.TempBuilder.SetMagicNumber(val, upVal);
            effect.RefreshCard();
        })
                .SetLimits(0, 999)
                .SetValue(effect.TempBuilder.magicNumber, effect.TempBuilder.magicNumberUpgrade);
        curW += SPACING_WIDTH;
        SecondaryValueEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , PGR.PCL.Strings.CardEditor.SecondaryNumber, (val, upVal) -> {
            effect.TempBuilder.SetSecondaryValue(val, upVal);
            effect.RefreshCard();
        })
                .SetLimits(0, 999)
                .SetValue(effect.TempBuilder.secondaryValue, effect.TempBuilder.secondaryValueUpgrade);
        curW += SPACING_WIDTH;
        HitCountEditor = (PCLCustomCardValueEditor) new PCLCustomCardValueEditor(new AdvancedHitbox(curW, ScreenH(0.77f), MENU_WIDTH / 4, MENU_HEIGHT)
                , PGR.PCL.Strings.CardEditor.HitCount, (val, upVal) -> {
            effect.TempBuilder.SetHitCount(val, upVal);
            effect.RefreshCard();
        })
                .SetLimits(1, 999)
                .SetValue(effect.TempBuilder.hitCount, effect.TempBuilder.hitCountUpgrade);
        TagsDropdown = new GUI_Dropdown<PCLCardTagHelper>(new AdvancedHitbox(ScreenW(0.79f), ScreenH(0.77f), MENU_WIDTH * 1.2f, MENU_HEIGHT))
                .SetOnChange(tags -> {
                    effect.TempBuilder.SetTags(PCLJUtils.Map(tags, tag -> tag.Tag), true);
                    effect.RefreshCard();
                })
                .SetLabelFunctionForOption(item -> {
                    return item.Tooltip.GetTitleOrIcon() + " " + item.Tooltip.title;
                }, true)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.Tags)
                .SetIsMultiSelect(true)
                .SetItems(PCLCardTagHelper.GetAll())
                .SetSelection(PCLJUtils.Map(effect.TempBuilder.tags, PCLCardTagHelper::Get), false);
        AttackTypeDropdown = new GUI_Dropdown<PCLAttackType>(new AdvancedHitbox(ScreenW(0.79f), ScreenH(0.64f), MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(targets -> {
                    if (!targets.isEmpty()) {
                        effect.TempBuilder.SetAttackType(targets.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetLabelFunctionForOption(c -> c.GetTooltip() != null ? c.GetTooltip().title : "", false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.AttackType)
                .SetCanAutosizeButton(true)
                .SetItems(PCLJUtils.Filter(PCLAttackType.values(), v -> v.GetTooltip() != null))
                .SetSelection(effect.TempBuilder.attackType, false);
        AttackEffectDropdown = new GUI_Dropdown<AbstractGameAction.AttackEffect>(new AdvancedHitbox(ScreenW(0.79f), ScreenH(0.56f), MENU_WIDTH, MENU_HEIGHT)
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnChange(targets -> {
                    if (!targets.isEmpty()) {
                        effect.TempBuilder.SetAttackEffect(targets.get(0));
                        effect.RefreshCard();
                    }
                })
                .SetLabelFunctionForOption(Enum::name, false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, PGR.PCL.Strings.CardEditor.AttackEffect)
                .SetCanAutosizeButton(true)
                .SetItems(AttackEffects.Keys())
                .SetSelection(effect.TempBuilder.attackEffect, false);

        // Affinity editors

        curW = START_X;
        upgrade_label2 = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(curW, ScreenH(0.64f) - MENU_HEIGHT, MENU_WIDTH / 4, MENU_HEIGHT))
                .SetAlignment(0.5f,0.0f,false)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.6f).SetColor(Settings.BLUE_TEXT_COLOR)
                .SetText(PGR.PCL.Strings.CardEditor.Upgrades);
        scaling_label = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(curW, ScreenH(0.64f) - MENU_HEIGHT * 2, MENU_WIDTH / 4, MENU_HEIGHT))
                .SetAlignment(0.5f,0.0f,false)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.6f).SetColor(Settings.BLUE_TEXT_COLOR)
                .SetText(PGR.PCL.Strings.SeriesUI.Scalings);
        curW += SPACING_WIDTH;
        for (PCLAffinity affinity : PCLAffinity.GetAvailableAffinities(effect.CurrentSlot.SlotColor)) {
            AffinityEditors.add((PCLCustomCardAffinityValueEditor) new PCLCustomCardAffinityValueEditor(new AdvancedHitbox(curW, ScreenH(0.64f), MENU_WIDTH / 4, MENU_HEIGHT)
                    , affinity, (af, val, upVal, scVal) -> {
                effect.TempBuilder.SetAffinity(af, val, upVal, scVal);
                effect.RefreshCard();
            })
                    .SetValue(effect.TempBuilder.affinities.GetLevel(affinity), effect.TempBuilder.affinities.GetUpgrade(affinity), effect.TempBuilder.affinities.GetScaling(affinity, false))
                    .ShowThirdValue(effect.CurrentSlot.SlotColor == PGR.Enums.Cards.THE_FOOL));
            curW += SPACING_WIDTH;
        }

        float curY = ScreenH(0.44f);
        for (int i = 0; i < EFFECT_COUNT; i++) {
            int finalI = i;
            EffectEditors.add(new PCLCustomCardEffectEditor(effect.TempBuilder, CurrentEffects.get(i), new AdvancedHitbox(START_X, curY, MENU_WIDTH / 4, MENU_HEIGHT)
                    , "", (be) -> {
                        CurrentEffects.set(finalI, be);
                        effect.TempBuilder.SetBaseEffect(CurrentEffects, false, true);
                        effect.RefreshCard();
                    }));
            curY -= SPACING_WIDTH;
        }

        cancel_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .SetPosition(buttonWidth * 0.6f, button_cY)
                .SetColor(Color.FIREBRICK)
                .SetText(GridCardSelectScreen.TEXT[1])
                .SetOnClick(effect::End);

        save_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .SetPosition(cancel_button.hb.cX, cancel_button.hb.y + cancel_button.hb.height + labelHeight * 0.8f)
                .SetColor(Color.WHITE)
                .SetText(GridCardSelectScreen.TEXT[0])
                .SetOnClick(effect::Save);
    }

    @Override
    public void Update() {
        CostEditor.TryUpdate();
        DamageEditor.TryUpdate();
        BlockEditor.TryUpdate();
        MagicNumberEditor.TryUpdate();
        SecondaryValueEditor.TryUpdate();
        HitCountEditor.TryUpdate();
        SeriesDropdown.TryUpdate();
        RaritiesDropdown.TryUpdate();
        TypesDropdown.TryUpdate();
        TagsDropdown.TryUpdate();
        TargetDropdown.TryUpdate();
        AttackTypeDropdown.TryUpdate();
        AttackEffectDropdown.TryUpdate();
        for (PCLCustomCardAffinityValueEditor aEditor : AffinityEditors) {
            aEditor.TryUpdate();
        }
        for (PCLCustomCardEffectEditor aEditor : EffectEditors) {
            aEditor.TryUpdate();
        }
        upgrade_label.TryUpdate();
        upgrade_label2.TryUpdate();
        scaling_label.TryUpdate();
        NameInput.TryUpdate();
        cancel_button.TryUpdate();
        save_button.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb) {
        upgrade_label.TryRender(sb);
        upgrade_label2.TryRender(sb);
        scaling_label.TryRender(sb);
        for (PCLCustomCardAffinityValueEditor aEditor : AffinityEditors) {
            aEditor.TryRender(sb);
        }
        for (PCLCustomCardEffectEditor aEditor : EffectEditors) {
            aEditor.TryRender(sb);
        }
        CostEditor.TryRender(sb);
        DamageEditor.TryRender(sb);
        BlockEditor.TryRender(sb);
        MagicNumberEditor.TryRender(sb);
        SecondaryValueEditor.TryRender(sb);
        HitCountEditor.TryRender(sb);
        AttackTypeDropdown.TryRender(sb);
        AttackEffectDropdown.TryRender(sb);
        TagsDropdown.TryRender(sb);
        RaritiesDropdown.TryRender(sb);
        TypesDropdown.TryRender(sb);
        TargetDropdown.TryRender(sb);
        SeriesDropdown.TryRender(sb);
        cancel_button.TryRender(sb);
        NameInput.TryRender(sb);
        save_button.TryRender(sb);
    }
}
