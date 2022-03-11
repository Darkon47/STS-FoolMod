package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect_Affinity;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class BaseEffect_PayAffinity extends BaseEffect_Affinity
{
    public static final String ID = Register(BaseEffect_PayAffinity.class, PGR.Enums.Cards.THE_FOOL);

    public BaseEffect_PayAffinity()
    {
        this(0);
    }

    public BaseEffect_PayAffinity(String[] content)
    {
        super(content);
    }

    public BaseEffect_PayAffinity(int amount, PCLAffinity... affinities)
    {
        super(ID, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Pay(amount, PCLJUtils.JoinStrings(" ", PCLJUtils.Map(affinities, PCLAffinity::GetTooltip)), true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLAffinity affinity : affinities) {
            PCLGameUtilities.TrySpendAffinity(affinity,amount,true);
        }
    }
}
