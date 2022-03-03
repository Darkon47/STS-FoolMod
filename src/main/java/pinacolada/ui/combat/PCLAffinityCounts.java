package pinacolada.ui.combat;

import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.powers.PCLCombatStats;

import java.util.Arrays;

public class PCLAffinityCounts {
    public int[] Counts = new int[PCLAffinity.Extended().length];

    public PCLAffinityCounts() {
    }

    public PCLAffinityCounts(PCLAffinityCounts counts) {
        Counts = counts.Counts.clone();
    }

    public PCLAffinityCounts(PCLCardAffinities affinities) {
        if (affinities.HasStar()) {
            for (PCLAffinity a : PCLAffinity.Extended()) {
                Counts[a.ID] = affinities.Star.level;
            }
        }
        else {
            for (PCLCardAffinity affinity : affinities.GetCardAffinities(true)) {
                Counts[affinity.type.ID] = affinity.level;
            }
        }

    }

    public PCLAffinityCounts Add(PCLAffinity affinity, int amount) {
        int actualAmount = PCLCombatStats.OnGainAffinity(affinity, amount, true);
        if (PCLAffinity.Star.equals(affinity) || PCLAffinity.General.equals(affinity)) {
            for (PCLAffinity a : PCLAffinity.Extended()) {
                Counts[a.ID] += actualAmount;
            }
        }
        else {
            Counts[affinity.ID] += actualAmount;
        }
        return this;
    }

    public PCLAffinityCounts Add(PCLAffinityCounts counts) {
        for (int i = 0; i < Counts.length; i++) {
            Counts[i] += PCLCombatStats.OnGainAffinity(PCLAffinity.Extended()[i], counts.Counts[i], true);
        }
        return this;
    }

    public PCLAffinityCounts Add(PCLCardAffinities affinities) {
        if (affinities.HasStar()) {
            int actualAmount = PCLCombatStats.OnGainAffinity(PCLAffinity.Star, affinities.Star.level, true);
            for (PCLAffinity a : PCLAffinity.Extended()) {
                Counts[a.ID] += actualAmount;
            }
        }
        else {
            for (PCLCardAffinity affinity : affinities.GetCardAffinities(true)) {
                Counts[affinity.type.ID] += PCLCombatStats.OnGainAffinity(affinity.type, affinity.level, true);
            }
        }

        return this;
    }

    public PCLAffinityCounts Add(PCLCardAffinities affinities, int amount) {
        if (affinities.HasStar()) {
            int actualAmount = PCLCombatStats.OnGainAffinity(PCLAffinity.Star, amount, true);
            for (PCLAffinity a : PCLAffinity.Extended()) {
                Counts[a.ID] += actualAmount;
            }
        }
        else {
            for (PCLCardAffinity affinity : affinities.GetCardAffinities(true)) {
                int actualAmount = PCLCombatStats.OnGainAffinity(affinity.type, amount, true);
                Counts[affinity.type.ID] += actualAmount;
            }
        }

        return this;
    }

    public PCLAffinityCounts Spend(PCLAffinity affinity, int amount) {
        if (PCLAffinity.Star.equals(affinity) || PCLAffinity.General.equals(affinity)) {
            for (PCLAffinity a : PCLAffinity.Extended()) {
                Counts[a.ID] -= amount;
                if (Counts[a.ID] < 0) {
                    Counts[a.ID] = 0;
                }
            }
        }
        else {
            Counts[affinity.ID] -= amount;
            if (Counts[affinity.ID] < 0) {
                Counts[affinity.ID] = 0;
            }
        }
        return this;
    }

    public int GetAmount(PCLAffinity affinity)
    {
        if (PCLAffinity.Star.equals(affinity))
        {
            return Arrays.stream(Counts).min().getAsInt();
        }
        else if (PCLAffinity.General.equals(affinity))
        {
            return Arrays.stream(Counts).max().getAsInt();
        }
        else
        {
            return Counts[affinity.ID];
        }
    }
}
