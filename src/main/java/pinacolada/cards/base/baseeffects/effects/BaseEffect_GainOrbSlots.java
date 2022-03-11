package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_GainOrbSlots extends BaseEffect
{
    public static final String ID = Register(BaseEffect_GainOrbSlots.class);

    public BaseEffect_GainOrbSlots()
    {
        this(0);
    }

    public BaseEffect_GainOrbSlots(int amount)
    {
        super(ID, null, PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PGR.Tooltips.OrbSlot, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainOrbSlots(amount);
    }
}
