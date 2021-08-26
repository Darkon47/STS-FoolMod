package eatyourbeets.powers.affinity;

import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class CorruptionPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(CorruptionPower.class);
    public static final String SECONDARY_ID = "~Crystallize";
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;

    protected static final int[] THRESHOLDS = new int[]{ 5, 7, 9, 11, 13 };

    public CorruptionPower()
    {
        super(AFFINITY_TYPE, POWER_ID, SECONDARY_ID);
    }

    @Override
    public int[] GetThresholds()
    {
        return THRESHOLDS;
    }

    @Override
    protected void OnThresholdReached(int thresholdIndex)
    {
        if (thresholdIndex == (GetThresholds().length - 1))
        {
            GameActions.Bottom.MakeCardInHand(new SummoningRitual());
            AnimatorCard_UltraRare.MarkAsSeen(SummoningRitual.DATA.ID);
        }
        else
        {
            GameActions.Bottom.MakeCardInDrawPile(new Crystallize());
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        int[] thresholds = GetThresholds();
        Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            final String card = (threshold == thresholds[thresholds.length - 1])
                    ? (JUtils.ModifyString(SummoningRitual.DATA.Strings.NAME, w -> "#p" + w))
                    : ("#y" + Crystallize.DATA.Strings.NAME);
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], threshold, card);
        }
        else
        {
            this.description = JUtils.Format(description, 0, 1, "");
        }

        this.tooltips.get(0).description = description;
    }
}