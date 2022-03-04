package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_Scry extends BaseEffect
{
    public static final String ID = Register(BaseEffect_Draw.class);

    public BaseEffect_Scry(int amount)
    {
        super(ID, null, PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Scry(amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Scry(amount);
    }
}
