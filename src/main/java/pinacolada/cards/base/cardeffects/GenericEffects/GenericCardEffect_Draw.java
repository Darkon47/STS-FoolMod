package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class GenericCardEffect_Draw extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_Draw.class);

    public GenericCardEffect_Draw(int amount)
    {
        super(ID, null,  PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Draw(amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(amount);
    }
}
