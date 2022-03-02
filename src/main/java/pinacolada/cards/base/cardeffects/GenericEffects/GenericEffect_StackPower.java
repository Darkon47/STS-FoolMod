package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericEffect_StackPower extends GenericEffect
{
    public static final String ID = Register(GenericEffect_StackPower.class);

    protected ArrayList<PCLPowerHelper> powers;

    public GenericEffect_StackPower(String[] content)
    {
        super(content);
        this.powers = PCLJUtils.Map(SplitEntityIDs(), PCLPowerHelper::Get);
    }

    public GenericEffect_StackPower(PCLCardTarget target, int amount, PCLPowerHelper... powers)
    {
        super(ID, JoinEntityIDs(powers, power -> power.ID), target, amount);
        this.powers = new ArrayList<>(Arrays.asList(powers));
    }

    public GenericEffect_StackPower Add(PCLPowerHelper newPo) {
        if (newPo != null) {
            this.powers.add(newPo);
            this.entityID = JoinEntityIDs(powers, power -> power.ID);
        }
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(powers, power -> power.Tooltip));
        switch (target) {
            case Random:
                return PGR.PCL.Strings.Actions.ApplyToRandom(amount, joinedString, true);
            case Normal:
                return PGR.PCL.Strings.Actions.Apply(amount, joinedString, true);
            case AoE:
                return PGR.PCL.Strings.Actions.ApplyToALL(amount, joinedString, true);
            default:
                return powers.size() > 0 && powers.get(0).EndTurnBehavior == PCLPowerHelper.Behavior.Temporary
                        ? PGR.PCL.Strings.Actions.GainTemporaryAmount(amount, joinedString, true)
                        : amount < 0 ? PGR.PCL.Strings.Actions.LosePower(amount, joinedString, true)
                        : PGR.PCL.Strings.Actions.GainAmount(amount, joinedString, true);
        }
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        for (PCLPowerHelper power : powers) {
            PCLActions.Bottom.StackPower(target.GetTarget(m), power, amount);
        }
    }
}
