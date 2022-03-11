package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_GainTempHP extends BaseEffect
{
    public static final String ID = Register(BaseEffect_GainTempHP.class);

    public BaseEffect_GainTempHP()
    {
        this(0);
    }

    public BaseEffect_GainTempHP(int amount)
    {
        super(ID, null, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PGR.Tooltips.TempHP, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(amount);
    }

    @Override
    public AbstractAttribute GetSpecialInfo() {
        return TempHPAttribute.Instance
                .SetText(new ColoredString(amount, Settings.CREAM_COLOR));
    }
}
