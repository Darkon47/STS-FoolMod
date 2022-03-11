package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_GainCardTempHP extends BaseEffect
{
    public static final String ID = Register(BaseEffect_GainCardTempHP.class);


    public BaseEffect_GainCardTempHP(PCLCard card)
    {
        super(ID, null, PCLCardTarget.Self, 0);
        SetSourceCard(card, PCLCardValueSource.MagicNumber);
    }

    @Override
    public String GetText()
    {
        return null;
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.GainAmount("X", PGR.Tooltips.TempHP.title, false);
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
