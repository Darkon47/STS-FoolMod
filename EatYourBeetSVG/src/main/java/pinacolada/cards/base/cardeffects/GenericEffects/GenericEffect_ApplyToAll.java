package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_ApplyToAll extends GenericEffect
{
    protected final TargetHelper target;
    protected final PCLPowerHelper power;

    public GenericEffect_ApplyToAll(TargetHelper target, PCLPowerHelper power, int amount)
    {
        this(target, power, power.Tooltip, amount);
    }

    public GenericEffect_ApplyToAll(TargetHelper target, PCLPowerHelper power, PCLCardTooltip tooltip, int amount)
    {
        this.target = target;
        this.power = power;
        this.tooltip = tooltip;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.ApplyToALL(amount, "["+tooltip.title+"]", true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(TargetHelper.Enemies(p), power, amount);
    }
}
