package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTagHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericEffect_ModifyCardTag extends GenericEffect
{
    public static final String ID = Register(GenericEffect_ModifyCardTag.class);

    protected ArrayList<PCLCardTagHelper> tags;
    protected ArrayList<AbstractCard> cards;

    public GenericEffect_ModifyCardTag(AbstractCard card, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = new ArrayList<>();
        this.cards.add(card);
    }


    public GenericEffect_ModifyCardTag(ArrayList<AbstractCard> cards, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = cards;
    }

    public GenericEffect_ModifyCardTag AddCard(AbstractCard card) {
        if (card != null) {
            this.cards.add(card);
        }
        return this;
    }

    public GenericEffect_ModifyCardTag AddTag(PCLCardTagHelper nt) {
        if (nt != null) {
            this.tags.add(nt);
            this.entityID = JoinEntityIDs(tags, tag -> tag.Tag.name());
        }
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(tags, tag -> tag.Tooltip));
        return PGR.PCL.Strings.Actions.Give1(joinedString, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : cards) {
            for (PCLCardTagHelper tag : tags) {
                PCLActions.Bottom.ModifyTag(c, tag.Tag, true);
            }
        }
    }
}
