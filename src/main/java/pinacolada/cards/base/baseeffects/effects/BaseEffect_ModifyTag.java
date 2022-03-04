package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTagHelper;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseEffect_ModifyTag extends BaseEffect
{
    public static final String ID = Register(BaseEffect_ModifyTag.class);

    protected ArrayList<PCLCardTagHelper> tags;
    protected ArrayList<AbstractCard> cards;

    public BaseEffect_ModifyTag(AbstractCard card, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = new ArrayList<>();
        this.cards.add(card);
    }


    public BaseEffect_ModifyTag(ArrayList<AbstractCard> cards, PCLCardTagHelper... tags)
    {
        super(ID, JoinEntityIDs(tags, tag -> tag.Tag.name()), PCLCardTarget.None, 1);
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.cards = cards;
    }

    public BaseEffect_ModifyTag AddCard(AbstractCard card) {
        if (card != null) {
            this.cards.add(card);
        }
        return this;
    }

    public BaseEffect_ModifyTag AddTag(PCLCardTagHelper nt) {
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
        return PGR.PCL.Strings.Actions.GiveTarget(PGR.PCL.Strings.Actions.Cards(cards.size()), joinedString, true);
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
