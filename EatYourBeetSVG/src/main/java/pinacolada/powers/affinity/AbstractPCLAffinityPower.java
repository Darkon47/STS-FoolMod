package pinacolada.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinityRow;
import pinacolada.utilities.PCLJUtils;
import pinacolada.utilities.PCLRenderHelpers;

import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class AbstractPCLAffinityPower extends PCLClickablePower
{
    public static final int BASE_THRESHOLD = 5;
    public static final Color ACTIVE_COLOR = new Color(0.5f, 1f, 0.5f, 1f);
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    //@Formatter: on

    public final PCLAffinity affinity;
    public final ArrayList<PCLCardTooltip> tooltips = new ArrayList<>();
    public float effectMultiplier;
    public int gainMultiplier;
    public int scalingMultiplier;
    public boolean forceEnableThisTurn;
    public boolean isActive;
    public Hitbox hb;
    protected int totalGainedThisCombat;
    protected int currentLevel;
    protected int bonusLevel;
    protected int nextGrantingLevel;
    protected int threshold = BASE_THRESHOLD;

    private static final StringBuilder builder = new StringBuilder();

    public AbstractPCLAffinityPower(PCLAffinity affinity, String powerID)
    {
        super(null, powerID, PowerTriggerConditionType.Special, BASE_THRESHOLD, null, null);

        this.affinity = affinity;

        //TODO: Add tooltip to EYBPower base class
        PCLCardTooltip tooltip = new PCLCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = new TextureRegion(img);
        tooltips.add(tooltip);

        FindTooltipsFromText(powerStrings.DESCRIPTIONS[1]);

        Initialize(null);
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.enabled = true;
        this.effectMultiplier = 1;
        this.gainMultiplier = 1;
        this.scalingMultiplier = 1;
        this.nextGrantingLevel = 1;
        this.currentLevel = 0;
        this.bonusLevel = 0;
        this.triggerCondition
                .SetUses(0, false, false)
                .SetCheckCondition(__ -> !isActive);

        Initialize(0, PowerType.BUFF, true);
    }

    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m) {}

    public void OnUse(AbstractMonster m, int cost) {
        this.isActive = true;
        flash();
    }

    public void AddLevel(int levels) {
        bonusLevel += levels;
        flashWithoutSound();
        TryGainLevelEffects();
    }

    public void AddUse(int uses) {
        if (triggerCondition.uses < triggerCondition.baseUses) {
            triggerCondition.uses = Math.min(triggerCondition.uses + uses, triggerCondition.baseUses);
            triggerCondition.Refresh(false);
            flash();
        }
    }

    public AbstractPCLAffinityPower SetEnabled(boolean enable)
    {
        this.enabled = enable;
        this.updateDescription();
        return this;
    }

    public AbstractPCLAffinityPower SetThreshold(int threshold) {
        this.threshold = threshold;
        this.updateDescription();
        return this;
    }

    public AbstractPCLAffinityPower SetEffectMultiplier(float effectMultiplier) {
        this.effectMultiplier = effectMultiplier;
        this.updateDescription();
        return this;
    }

    public AbstractPCLAffinityPower SetGainMultiplier(int gainMultiplier) {
        this.gainMultiplier = gainMultiplier;
        this.updateDescription();
        return this;
    }

    public AbstractPCLAffinityPower SetScalingMultiplier(int scalingMultiplier) {
        this.scalingMultiplier = scalingMultiplier;
        this.updateDescription();
        return this;
    }

    public void Stack(int amount, boolean maintain)
    {
        if (!IsEnabled())
        {
            return;
        }

        if (maintain)
        {
            Maintain();
        }

        amount *= this.gainMultiplier;
        super.stackPower(amount, false);
        this.totalGainedThisCombat += amount;
        while (this.amount >= GetEffectiveThreshold()) {
            currentLevel += 1;
            flashWithoutSound();
            TryGainLevelEffects();
        }
    }

    public Integer GetEffectiveScaling() {
        return GetEffectiveLevel() * this.scalingMultiplier;
    }

    protected float GetEffectiveIncrease() {
        return this.effectMultiplier;
    }

    public Integer GetEffectiveLevel() {
        return Math.max(0, currentLevel + bonusLevel);
    }

    public Integer GetNextGrantingLevel() {return nextGrantingLevel;}

    protected float GetEffectiveMultiplier() {
        return 1f + GetEffectiveIncrease();
    }

    public Integer GetEffectiveThreshold() {return threshold * (currentLevel + 1);}

    @Override
    public String GetUpdatedDescription()
    {
        String newDesc = FormatDescription(0, PCLAffinityRow.SYNERGY_MULTIPLIER, GetEffectiveThreshold(), GetNextGrantingLevel(), GetMultiplierForDescription(), !IsEnabled() ? powerStrings.DESCRIPTIONS[1] : "");
        if (this.tooltips.size() > 0) {
            this.tooltips.get(0).description = newDesc;
        }
        return newDesc;
    }

    protected int GetMultiplierForDescription() {
        return (int) (GetEffectiveIncrease() * 100);
    }

    public boolean IsEnabled() {
        return enabled || forceEnableThisTurn;
    }

    public boolean CanSpend(int amount) {
        return IsEnabled() && this.amount >= amount;
    }

    public boolean TrySpend(int amount)
    {
        if (CanSpend(amount)) {
            super.stackPower(-amount, false);

            return true;
        }
        return false;
    }

    protected void TryGainLevelEffects() {
        while (GetEffectiveLevel() >= nextGrantingLevel) {
            nextGrantingLevel *= 2;
            triggerCondition.AddUses(1);
            flash();
        }
    }

    public void Maintain() {
        this.forceEnableThisTurn = true;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.forceEnableThisTurn = false;
        isActive = false;
    }

    public void Render(SpriteBatch sb)
    {
        final float scale = Settings.scale;
        final float scale2 = Settings.scale * 1.5f;
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + (5 * scale);
        final float y = hb.y + (9 * scale);
        final float cX = hb.cX + (34 * scale);
        final float cY = hb.cY;
        final float cX2 = hb.cX + (98 * scale);

        Integer level = GetEffectiveLevel();
        Integer threshold = GetEffectiveThreshold();
        Color amountColor = !IsEnabled() ? Colors.Cream(0.6f) : Colors.White(1f);
        Color usesColor = triggerCondition.uses > 0 ? Colors.Gold(1).cpy() : Colors.Cream(0.6f);
        Color levelColor = level > 0 ? Colors.Green(1).cpy() : Colors.Cream(0.6f);
        PCLRenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.PCL.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale2, h / scale, 1, 0);
        if (effectMultiplier > 1)
        {
            PCLRenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.PCL.Images.Panel_Elliptical_Half_H.Texture(), cX2 , cY, (w / scale2) + 8, (h / scale) + 8, 1, 0);
            PCLRenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.PCL.Images.Panel_Elliptical_Half_H.Texture(), cX2, cY, w / scale2, h / scale, 1, 0);
        }
        else
        {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.PCL.Images.Panel_Elliptical_Half_H.Texture(), cX2, cY, w / scale2, h / scale, 1, 0);
        }

        final Color imgColor = Colors.White(IsEnabled() ? 1 : 0.5f);
        final Color borderColor = isActive ? ACTIVE_COLOR : (enabled && triggerCondition.CanUse()) ? imgColor : disabledColor;

        super.renderIconsImpl(sb, x + 16 * scale, cY + (3f * scale), borderColor, imgColor);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "L" + String.valueOf(level) + "/" + String.valueOf(nextGrantingLevel), x + 36 * scale, y - 8 * scale, fontScale * 0.86f, levelColor);

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + (threshold < 10 ? 90 : 95) * scale, y, 1, amountColor);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 64 * scale, y, fontScale, amountColor);

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + triggerCondition.baseUses, x + (triggerCondition.baseUses < 10 ? 154 : 159) * scale, y, 1, usesColor);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(triggerCondition.uses), x + 128 * scale, y, 1, usesColor);

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x + w + (5 * scale), cY + (5f * scale));
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        hb.update();

        if (hb.hovered)
        {
            PCLCardTooltip.QueueTooltips(tooltips, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f));
        }
    }

    protected void FindTooltipsFromText(String text) {

        boolean foundIcon = false;
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if (foundIcon) {
                if (']' != c)
                {
                    builder.append(c);
                    continue;
                }
                foundIcon = false;
                PCLCardTooltip tooltip = CardTooltips.FindByID(PCLJUtils.InvokeBuilder(builder));
                if (tooltip != null) {
                    tooltips.add(tooltip);
                }
            }
            else if ('[' == c)
            {
                foundIcon = true;
            }
        }

    }
}