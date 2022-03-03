package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericCardEffect_PayAffinity extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_PayAffinity.class);

    protected ArrayList<PCLAffinity> affinities;

    public GenericCardEffect_PayAffinity(int amount, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, affinity -> affinity.Name), PCLCardTarget.None, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
    }

    public GenericCardEffect_PayAffinity Add(PCLAffinity newAf) {
        this.affinities.add(newAf);
        this.entityID = JoinEntityIDs(affinities, af -> af.Name);
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(affinities, PCLAffinity::GetTooltip));
        return PGR.PCL.Strings.Actions.PayCost(amount, joinedString, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLAffinity affinity : affinities) {
            PCLGameUtilities.TrySpendAffinity(affinity,amount,true);
        }
    }
}
