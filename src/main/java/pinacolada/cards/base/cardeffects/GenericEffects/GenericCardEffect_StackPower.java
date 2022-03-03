package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericCardEffect_StackPower extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_StackPower.class);

    protected ArrayList<PCLPowerHelper> powers;

    public GenericCardEffect_StackPower(String[] content)
    {
        super(content);
        this.powers = PCLJUtils.Map(SplitEntityIDs(), PCLPowerHelper::Get);
    }

    public GenericCardEffect_StackPower(PCLCardTarget target, int amount, PCLPowerHelper... powers)
    {
        super(ID, JoinEntityIDs(powers, power -> power.ID), target, amount);
        this.powers = new ArrayList<>(Arrays.asList(powers));
    }

    public GenericCardEffect_StackPower Add(PCLPowerHelper newPo) {
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
                return powers.size() > 0 && powers.get(0).IsDebuff ? PGR.PCL.Strings.Actions.ApplyToRandom(amount, joinedString, true) : PGR.PCL.Strings.Actions.GiveRandomEnemy(amount, joinedString, true);
            case Normal:
                return powers.size() > 0 && powers.get(0).IsDebuff ? PGR.PCL.Strings.Actions.Apply(amount, joinedString, true) : PGR.PCL.Strings.Actions.Give2(amount, joinedString, true);
            case AoE:
                return powers.size() > 0 && powers.get(0).IsDebuff ? PGR.PCL.Strings.Actions.ApplyToALL(amount, joinedString, true) :  PGR.PCL.Strings.Actions.GiveAllEnemies(amount, joinedString, true);
            default:
                return powers.size() > 0 && powers.get(0).EndTurnBehavior == PCLPowerHelper.Behavior.Temporary
                        ? PGR.PCL.Strings.Actions.GainTemporaryAmount(amount, joinedString, true)
                        : amount < 0 ? PGR.PCL.Strings.Actions.LosePower(amount, joinedString, true)
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
