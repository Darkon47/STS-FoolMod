package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTagHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericCardEffect_ModifyCardTag extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_ModifyCardTag.class);

    protected ArrayList<PCLCardTagHelper> tags;
    protected ArrayList<AbstractCard> cards;

    public GenericCardEffect_ModifyCardTag(AbstractCard card, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = new ArrayList<>();
        this.cards.add(card);
    }


    public GenericCardEffect_ModifyCardTag(ArrayList<AbstractCard> cards, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = cards;
    }

    public GenericCardEffect_ModifyCardTag AddCard(AbstractCard card) {
        if (card != null) {
            this.cards.add(card);
        }
        return this;
    }

    public GenericCardEffect_ModifyCardTag AddTag(PCLCardTagHelper nt) {
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
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractCard c : cards) {
            for (PCLCardTagHelper tag : tags) {
                PCLActions.Bottom.ModifyTag(c, tag.Tag, true);
            }
        }
    }
}
