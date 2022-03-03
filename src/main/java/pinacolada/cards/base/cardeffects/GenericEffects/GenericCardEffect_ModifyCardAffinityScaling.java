package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericCardEffect_ModifyCardAffinityScaling extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_ModifyCardAffinityScaling.class);

    protected ArrayList<PCLAffinity> affinities;
    protected ArrayList<AbstractCard> cards;

    public GenericCardEffect_ModifyCardAffinityScaling(int amount, ArrayList<AbstractCard> cards, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, affinity -> affinity.Name), PCLCardTarget.None, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
        this.cards = cards;
    }

    public GenericCardEffect_ModifyCardAffinityScaling AddCard(AbstractCard card) {
        if (card != null) {
            this.cards.add(card);
        }
        return this;
    }

    public GenericCardEffect_ModifyCardAffinityScaling AddAffinity(PCLAffinity newAf) {
        if (newAf != null) {
            this.affinities.add(newAf);
            this.entityID = JoinEntityIDs(affinities, af -> af.Name);
        }
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(", " + amount + " ", PCLJUtils.Map(affinities, af -> CardTooltips.FindByID(af.GetScalingTooltipID()).GetTitleOrIcon()));
        return PGR.PCL.Strings.Actions.Give2(amount, joinedString, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractCard c : cards) {
            for (PCLAffinity affinity : affinities) {
                PCLActions.Bottom.IncreaseScaling(c, affinity, amount);
            }
        }
    }
}
