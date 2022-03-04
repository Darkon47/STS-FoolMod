package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseEffect_PayAffinity extends BaseEffect
{
    public static final String ID = Register(BaseEffect_PayAffinity.class);

    protected ArrayList<PCLAffinity> affinities;

    public BaseEffect_PayAffinity(String[] content)
    {
        super(content);
        this.affinities = ParseAffinitiesFromEntityID();
    }

    public BaseEffect_PayAffinity(int amount, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, Enum::name), PCLCardTarget.None, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
    }

    public BaseEffect_PayAffinity Add(PCLAffinity newAf) {
        this.affinities.add(newAf);
        this.entityID = JoinEntityIDs(affinities, Enum::name);
        return this;
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
