package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_GainTempHP extends GenericEffect
{
    public static final String ID = Register(GenericEffect_GainTempHP.class);

    public GenericEffect_GainTempHP(int amount)
    {
        super(ID, null, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PGR.Tooltips.TempHP, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainTemporaryHP(amount);
    }
}
