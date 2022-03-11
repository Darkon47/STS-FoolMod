package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.CardTooltips;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseEffect_ModifyAffinityScaling extends BaseEffect
{
    public static final String ID = Register(BaseEffect_ModifyAffinityScaling.class, PGR.Enums.Cards.THE_FOOL);

    protected ArrayList<PCLAffinity> affinities;
    protected ArrayList<AbstractCard> cards;

    public BaseEffect_ModifyAffinityScaling()
    {
        this(0, new ArrayList<>());
    }

    public BaseEffect_ModifyAffinityScaling(SerializedData content)
    {
        super(content);
        this.affinities = ParseAffinitiesFromEntityID();
        this.cards = new ArrayList<>();
    }

    public BaseEffect_ModifyAffinityScaling(int amount, ArrayList<AbstractCard> cards, PCLAffinity... affinities)
    {
        super(ID, JoinEntityIDs(affinities, Enum::name), PCLCardTarget.None, amount);
        this.affinities = new ArrayList<>(Arrays.asList(affinities));
        this.cards = cards;
    }

    public BaseEffect_ModifyAffinityScaling AddCard(AbstractCard card) {
        if (card != null) {
            this.cards.add(card);
        }
        return this;
    }

    public BaseEffect_ModifyAffinityScaling Set(PCLAffinity... affinities) {
        return Set(Arrays.asList(affinities));
    }

    public BaseEffect_ModifyAffinityScaling Set(List<PCLAffinity> affinities) {
        this.affinities.clear();
        this.affinities.addAll(affinities);
        this.entityID = JoinEntityIDs(affinities, af -> af.Name);
        return this;
    }

    public BaseEffect_ModifyAffinityScaling AddAffinity(PCLAffinity newAf) {
        if (newAf != null) {
            this.affinities.add(newAf);
            this.entityID = JoinEntityIDs(affinities, Enum::name);
        }
        return this;
    }

    public ArrayList<PCLAffinity> GetAffinities() {
        return affinities;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(", " + amount + " ", PCLJUtils.Map(affinities, af -> CardTooltips.FindByID(af.GetScalingTooltipID()).GetTitleOrIcon()));
        return PGR.PCL.Strings.Actions.GiveTargetAmount(PGR.PCL.Strings.Actions.Cards(cards.size()), amount, joinedString, true);
    }

    public String GetSampleText() {
        return PGR.PCL.Strings.Actions.GiveTarget(PGR.PCL.Strings.Actions.Cards("X"), PGR.PCL.Strings.SeriesUI.Scalings, false);
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
