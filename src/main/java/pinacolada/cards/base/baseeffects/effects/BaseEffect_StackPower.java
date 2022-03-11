package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BaseEffect_StackPower extends BaseEffect
{
    public static final String ID = Register(BaseEffect_StackPower.class);

    protected ArrayList<PCLPowerHelper> powers;

    public BaseEffect_StackPower()
    {
        this(PCLCardTarget.Self, 0);
    }

    public BaseEffect_StackPower(String[] content)
    {
        super(content);
        this.powers = PCLJUtils.Filter(PCLJUtils.Map(SplitEntityIDs(), PCLPowerHelper::Get), Objects::nonNull);
    }

    public BaseEffect_StackPower(PCLCardTarget target, int amount, PCLPowerHelper... powers)
    {
        super(ID, JoinEntityIDs(powers, power -> power.ID), target, amount);
        this.powers = new ArrayList<>(Arrays.asList(powers));
    }

    public BaseEffect_StackPower Set(PCLPowerHelper... powers) {
        return Set(Arrays.asList(powers));
    }

    public BaseEffect_StackPower Set(List<PCLPowerHelper> powers) {
        this.powers.clear();
        this.powers.addAll(powers);
        this.entityID = JoinEntityIDs(powers, power -> power.ID);
        return this;
    }

    public BaseEffect_StackPower Add(PCLPowerHelper newPo) {
        if (newPo != null) {
            this.powers.add(newPo);
            this.entityID = JoinEntityIDs(powers, power -> power.ID);
        }
        return this;
    }

    public ArrayList<PCLPowerHelper> GetPowers() {
        return powers;
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            for (PCLPowerHelper power : powers) {
                PCLGameUtilities.GetPCLIntent(m).AddModifier(power.ID, amount);
            }
        }
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(powers, power -> power.Tooltip));
        switch (target) {
            case Random:
            case AoE:
                return powers.size() > 0 && powers.get(0).IsDebuff ? PGR.PCL.Strings.Actions.ApplyAmountToTarget(amount, joinedString, GetTargetString(), true) : PGR.PCL.Strings.Actions.GiveTargetAmount(GetTargetString(), amount, joinedString, true);
            case Normal:
                return powers.size() > 0 && powers.get(0).IsDebuff ? PGR.PCL.Strings.Actions.ApplyAmount(amount, joinedString, true) : PGR.PCL.Strings.Actions.GiveTargetAmount(GetTargetString(), amount, joinedString, true);
            default:
                return powers.size() > 0 && powers.get(0).EndTurnBehavior == PCLPowerHelper.Behavior.Temporary
                        ? PGR.PCL.Strings.Actions.HaveTemporaryAmount(amount, joinedString, true)
                        : amount < 0 ? PGR.PCL.Strings.Actions.LoseAmount(amount, joinedString, true)
                        : PGR.PCL.Strings.Actions.GainAmount(amount, joinedString, true);
        }
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLPowerHelper power : powers) {
            PCLActions.Bottom.StackPower(target.GetTarget(m), power, amount);
        }
    }
}
