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

public class BaseEffect_GainAffinityPower extends BaseEffect_Affinity
{
    public static final String ID = Register(BaseEffect_GainAffinityPower.class, PGR.Enums.Cards.THE_FOOL);

    public BaseEffect_GainAffinityPower()
    {
        this(0);
    }

    public BaseEffect_GainAffinityPower(SerializedData content)
    {
        super(content);
    }

    public BaseEffect_GainAffinityPower(int amount, PCLAffinity... affinities)
    {
        super(ID, PCLCardTarget.Self, amount);
    }


    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PCLJUtils.JoinStrings(" ", PCLJUtils.Map(affinities, PCLAffinity::GetPowerTooltip)), true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.GainAmount("X", PGR.Tooltips.Affinity_Power.title, false);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLAffinity affinity : affinities) {
            PCLActions.Bottom.StackAffinityPower(affinity, amount);
        }

    }
}
