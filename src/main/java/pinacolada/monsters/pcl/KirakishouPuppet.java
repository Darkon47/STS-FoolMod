package pinacolada.monsters.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.monsters.PCLAlly;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.special.ChangeIntentPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KirakishouPuppet extends PCLAlly {

    public static final int AMOUNT_AFFINITY = 2;
    public static final int BASE_HP = 25;
    public static final int BASE_ATTACK = 2;
    public static final int BASE_BLOCK = 1;
    public static final int COST_AFFINITY = 1;
    public static final int COST_BUFF = 2;
    public static final int COST_DEBUFF = 2;
    public static final int COST_ORB = 2;

    public final ArrayList<AbstractCard> cards = new ArrayList<>();
    public final PCLCardAffinities cardAffinities = new PCLCardAffinities(null);
    public HashMap<PCLAffinity, Integer> affinityPowers = new HashMap<>();
    public HashMap<PCLOrbHelper, Integer> orbs = new HashMap<>();
    public HashMap<PCLPowerHelper, Integer> buffs = new HashMap<>();
    public HashMap<PCLPowerHelper, Integer> debuffs = new HashMap<>();
    public PCLAffinity target = PCLAffinity.General;

    protected boolean canIntercept;

    private static HashMap<PCLAffinity, Integer> GetAffinityPowers(AbstractCard card, int limit) {
        HashMap<PCLAffinity, Integer> affs = new HashMap<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLAffinity foundAf = PCLJUtils.Find(PCLAffinity.Extended(), af -> af.GetTooltip().id.equals(tip.id));
                if (foundAf != null) {
                    affs.merge(foundAf, AMOUNT_AFFINITY, Integer::sum);
                    if (affs.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return affs;
    }

    private static HashMap<PCLPowerHelper, Integer> GetCommonBuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  PCLGameUtilities.GetPCLCommonBuffs());
    }

    private static HashMap<PCLPowerHelper, Integer> GetCommonDebuffs(AbstractCard card, int limit) {
        return GetCommonPowers(card, limit,  PCLGameUtilities.GetPCLCommonDebuffs());
    }

    private static HashMap<PCLPowerHelper, Integer> GetCommonPowers(AbstractCard card, int limit, ArrayList<PCLPowerHelper> source) {
        HashMap<PCLPowerHelper, Integer> powers = new HashMap<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLPowerHelper foundPower = PCLJUtils.Find(source, ph -> ph.Tooltip.id.equals(tip.id));
                if (foundPower != null) {
                    powers.merge(foundPower, 1, Integer::sum);
                    if (powers.size() >= limit) {
                        break;
                    }
                }
            }
        }

        return powers;
    }

    private static HashMap<PCLOrbHelper, Integer> GetOrbs(AbstractCard card, int limit) {
        HashMap<PCLOrbHelper, Integer> orbs = new HashMap<>();
        if (card instanceof PCLCard) {
            for (PCLCardTooltip tip : ((PCLCard) card).tooltips)
            {
                PCLOrbHelper found = PCLJUtils.Find(PCLOrbHelper.ALL.values(), o -> o.Tooltip.id.equals(tip.id));
                if (found != null) {
                    orbs.merge(found, 1, Integer::sum);
                    if (orbs.size() >= limit) {
                        break;
                    }
                }
            }
        }
        return orbs;
    }

    protected static SummonData GetSummonData(ArrayList<AbstractCard> cards) {
        SummonData summonData = new SummonData();
        summonData.Health = BASE_HP + PCLJUtils.SumInt(cards, c -> c.cost * (c.rarity == AbstractCard.CardRarity.RARE ? 6 : 3));
        summonData.Strength = BASE_ATTACK + PCLJUtils.SumInt(cards, c -> c.baseDamage) / 3;
        summonData.Dexterity = BASE_BLOCK + PCLJUtils.SumInt(cards, c -> c.baseBlock) / 3;
        summonData.Slot = CombatStats.Dolls.GetAvailableSlot();
        summonData.Intent = StartingIntent.Random;
        if (summonData.Slot < 0) {
            summonData.Slot = 0;
        }
        summonData.Position = CombatStats.Dolls.GetSlotPosition(summonData.Slot);
        return summonData;
    }


    public KirakishouPuppet(ArrayList<AbstractCard> cards) {
        super(GetSummonData(cards));
        AddActions();
        for (AbstractCard c : cards) {
            SetPropertiesFromCard(c);
        }
    }

    public void SetPropertiesFromCard(AbstractCard card) {
        cards.add(card);

        int rarityModifier = PCLGameUtilities.IsHighCost(card) ? 2 : 1;
        switch (card.rarity) {
            case RARE:
            case SPECIAL:
                rarityModifier += 2;
                break;
            case UNCOMMON:
            case CURSE:
                rarityModifier += 1;
                break;
        }

        affinityPowers = GetAffinityPowers(card, rarityModifier);
        buffs = GetCommonBuffs(card, rarityModifier -= affinityPowers.size());
        debuffs = GetCommonDebuffs(card, rarityModifier -= buffs.size());
        orbs = GetOrbs(card, rarityModifier -= debuffs.size());


        PCLCardAffinities other = PCLGameUtilities.GetPCLAffinities(card);
        if (other != null) {
            cardAffinities.Add(other);
            target = cardAffinities.GetLevel(PCLAffinity.General) <= 0 || cardAffinities.GetLevel(PCLAffinity.Star) > 0 ? PCLAffinity.General : PCLJUtils.FindMax(cardAffinities.List, af -> af.level).type;
        }
    }

    public void AddActions() {
        //TODO fix the descriptions

        this.specialMoves.put(Intent.DEBUFF, new ChangeIntentPower(this, Intent.DEBUFF, __ -> {
            for (Map.Entry<PCLAffinity, Integer> af : affinityPowers.entrySet()) {
                PCLActions.Bottom.StackAffinityPower(af.getKey(), af.getValue(), false);
            }
        }, power ->  {
            power.actionString = GR.PCL.Strings.Actions.GainAmount(StringUtils.join(PCLJUtils.Map(affinityPowers.keySet(), item -> item + " " + affinityPowers.get(item))), "", true);
            power.triggerCondition.requiredAmount = COST_AFFINITY + PCLJUtils.SumInt(affinityPowers.values(), i -> i);
            power.triggerCondition.affinities = new PCLAffinity[] {target};
        }
        , ImageMaster.INTENT_DEBUFF).AddToMoveset(moveset));
        this.specialMoves.put(Intent.BUFF, new ChangeIntentPower(this, Intent.BUFF, __ -> {
            for (Map.Entry<PCLPowerHelper, Integer> ph : buffs.entrySet()) {
                PCLActions.Bottom.ApplyPower(TargetHelper.Player(), ph.getKey(), ph.getValue());
            }
        }, power ->  {
            power.actionString = GR.PCL.Strings.Actions.GainAmount(StringUtils.join(PCLJUtils.Map(buffs.keySet(), item -> item + " " + buffs.get(item))), "", true);
            power.triggerCondition.requiredAmount = COST_BUFF + PCLJUtils.SumInt(buffs.values(), i -> i);
            power.triggerCondition.affinities = new PCLAffinity[] {target};
        }, ImageMaster.INTENT_BUFF).AddToMoveset(moveset));
        this.specialMoves.put(Intent.STRONG_DEBUFF, new ChangeIntentPower(this, Intent.STRONG_DEBUFF, __ -> {
            for (Map.Entry<PCLPowerHelper, Integer> ph : debuffs.entrySet()) {
                PCLActions.Bottom.ApplyPower(TargetHelper.RandomEnemy(), ph.getKey(), ph.getValue());
            }
        }, power ->  {
            power.actionString = GR.PCL.Strings.Actions.Apply(StringUtils.join(PCLJUtils.Map(debuffs.keySet(), item -> item + " " + debuffs.get(item))), GR.PCL.Strings.Actions.ToARandomEnemy, true);
            power.triggerCondition.requiredAmount = COST_DEBUFF + PCLJUtils.SumInt(debuffs.values(), i -> i);
            power.triggerCondition.affinities = new PCLAffinity[] {target};
        }, ImageMaster.INTENT_DEBUFF2).AddToMoveset(moveset));
        this.specialMoves.put(Intent.MAGIC, new ChangeIntentPower(this, Intent.MAGIC, __ -> {
            for (Map.Entry<PCLOrbHelper, Integer> o : orbs.entrySet()) {
                PCLActions.Bottom.ChannelOrbs(o.getKey(), o.getValue());
            }
        }, power ->  {
            power.actionString = GR.PCL.Strings.Actions.Channel(StringUtils.join(PCLJUtils.Map(orbs.keySet(), item -> item + " " + orbs.get(item))), "", true);
            power.triggerCondition.requiredAmount = COST_ORB + PCLJUtils.SumInt(orbs.entrySet(), e -> e.getValue() * e.getKey().weight);
            power.triggerCondition.affinities = new PCLAffinity[] {target};
        }, ImageMaster.INTENT_MAGIC).AddToMoveset(moveset));
    }

    public boolean CanIntercept() {
        return canIntercept;
    }
}
