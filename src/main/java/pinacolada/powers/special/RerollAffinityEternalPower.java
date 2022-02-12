package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLJUtils;

public class RerollAffinityEternalPower extends PCLClickablePower
{
    public static final String POWER_ID = PGR.PCL.CreateID(RerollAffinityEternalPower.class.getSimpleName());
    public boolean canChoose;

    public RerollAffinityEternalPower(int amount)
    {
        super(null, POWER_ID, PowerTriggerConditionType.None, 0);

        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;
        this.img = PGR.PCL.Images.Affinities.General.Texture();

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        PCLAffinity af = PCLCombatStats.MatchingSystem.ResolveMeter.GetCurrentAffinity();
        return FormatDescription(0,
                af.GetAffinitySymbol(),
                PCLJUtils.Format(powerStrings.DESCRIPTIONS[PCLAffinity.General == af || PCLAffinity.Silver == af ? 2 : 1], af.GetAffinitySymbol(), PCLCombatStats.MatchingSystem.ResolveMeter.ResolveGain));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
    }

    @Override
    public void updateDescription()
    {
        tooltip.description = description = GetUpdatedDescription();

        int uses = triggerCondition.uses;
        if (uses >= 0 && PGR.IsLoaded())
        {
            tooltip.subText.color = uses == 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
            tooltip.subText.text = uses + "/" + triggerCondition.baseUses + " " + PGR.PCL.Strings.Combat.Rerolls;
        }
    }

    @Override
    public void OnClick() {
        if (triggerCondition.CanUse())
        {
            PCLCombatStats.MatchingSystem.ResolveMeter.AdvanceAffinities(1);
            this.triggerCondition.uses -= 1;
            this.triggerCondition.Refresh(false);
            updateDescription();
        }
    }

    public RerollAffinityEternalPower SetCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
        return this;
    }

}