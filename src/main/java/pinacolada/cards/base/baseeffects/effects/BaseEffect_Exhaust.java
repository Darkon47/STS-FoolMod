package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardGroupHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect_CardGroup;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class BaseEffect_Exhaust extends BaseEffect_CardGroup
{
    public static final String ID = Register(BaseEffect_Exhaust.class);

    public BaseEffect_Exhaust()
    {
        this(0, (PCLCardGroupHelper) null);
    }

    public BaseEffect_Exhaust(SerializedData content)
    {
        super(content);
    }

    public BaseEffect_Exhaust(int amount, PCLCardGroupHelper... h)
    {
        super(ID, PCLCardTarget.None, amount, h);
    }

    @Override
    public String GetText()
    {
        return !groupTypes.isEmpty() ? PGR.PCL.Strings.Actions.ExhaustFrom(amount, JoinWithOr(PCLJUtils.Map(groupTypes, g -> g.CardString)), true) : PGR.PCL.Strings.Actions.ChannelRandomOrbs(amount, true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.Exhaust("X", false);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromPile(sourceCard != null ? sourceCard.name : "", amount, PCLJUtils.Map(groupTypes, PCLCardGroupHelper::GetCardGroup).toArray(new CardGroup[]{}));
    }
}
