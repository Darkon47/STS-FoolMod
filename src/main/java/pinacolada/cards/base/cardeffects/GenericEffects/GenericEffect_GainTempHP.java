package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_GainTempHP extends GenericEffect
{
    public GenericEffect_GainTempHP(int amount)
    {
        this.amount = amount;
        this.tooltip = GR.Tooltips.TempHP;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainTemporaryHP(amount);
    }
}
