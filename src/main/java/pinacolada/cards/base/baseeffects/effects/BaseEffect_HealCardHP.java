package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_HealCardHP extends BaseEffect
{
    public static final String ID = Register(BaseEffect_HealCardHP.class);


    public BaseEffect_HealCardHP(PCLCard card)
    {
        super(ID, null, PCLCardTarget.Self, 0);
        SetSourceCard(card, PCLCardValueSource.SecondaryNumber);
    }

    @Override
    public String GetText()
    {
        return null;
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.Heal("X", false);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Heal(amount);
    }

    @Override
    public AbstractAttribute GetSpecialInfo() {
        return HPAttribute.Instance
                .SetText(new ColoredString(amount, Settings.CREAM_COLOR));
    }
}
