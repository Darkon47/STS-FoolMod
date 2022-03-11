package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect_Affinity;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class BaseEffect_GainAffinity extends BaseEffect_Affinity
{
    public static final String ID = Register(BaseEffect_GainAffinity.class, PGR.Enums.Cards.THE_FOOL);

    public BaseEffect_GainAffinity()
    {
        this(0);
    }

    public BaseEffect_GainAffinity(String[] content)
    {
        super(content);
    }

    public BaseEffect_GainAffinity(int amount, PCLAffinity... affinities)
    {
        super(ID, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PCLJUtils.JoinStrings(" ", PCLJUtils.Map(affinities, PCLAffinity::GetTooltip)), true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLAffinity affinity : affinities) {
            PCLActions.Bottom.AddAffinity(affinity, amount);
        }
    }
}
