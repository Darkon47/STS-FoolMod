package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.PGR;
import pinacolada.ui.combat.FoolAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class RerollAffinityPower extends PCLClickablePower
{
    public static final String POWER_ID = PGR.PCL.CreateID(RerollAffinityPower.class.getSimpleName());
    public boolean canChoose;

    public RerollAffinityPower(int amount)
    {
        super(null, POWER_ID, PowerTriggerConditionType.None, 0);

        this.triggerCondition.SetUses(amount, true, true);
        this.hideAmount = true;
        this.img = PGR.PCL.Images.Affinities.General.Texture();

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        String afSymbol = PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity().GetAffinitySymbol();
        return FormatDescription(0,
                afSymbol,
                PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity().GetAffinitySymbol(),
                PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentMatchCombo(),
                PCLJUtils.Format(powerStrings.DESCRIPTIONS[PCLAffinity.Star.equals(PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity()) ? 2 : 1], afSymbol));
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
            PCLAffinity next = PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity();
            PCLActions.Bottom.RerollAffinity(FoolAffinityMeter.TARGET_NEXT).SetOptions(!canChoose, true);
            PCLActions.Bottom.RerollAffinity(FoolAffinityMeter.TARGET_CURRENT).SetAffinityChoices(next).SetOptions(true, true);
            this.triggerCondition.uses -= 1;
            this.triggerCondition.Refresh(false);
            updateDescription();
        }
    }

    public RerollAffinityPower SetCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
        return this;
    }

}