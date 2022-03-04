package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseEffect_Obtain extends BaseEffect
{
    public static final String ID = Register(BaseEffect_Obtain.class);

    protected ArrayList<PCLCardData> cardData;
    protected int upgradeTimes;

    public BaseEffect_Obtain(String[] content)
    {
        super(content);
        this.upgradeTimes = Integer.parseInt(misc);
    }

    public BaseEffect_Obtain(int copies, int upgradeTimes, PCLCardData... cards)
    {
        super(ID, JoinEntityIDs(cards, card -> card.ID), PCLCardTarget.Self, copies, String.valueOf(upgradeTimes));
        this.cardData = new ArrayList<>(Arrays.asList(cards));
    }

    public BaseEffect_Obtain Add(PCLCardData newCard) {
        this.cardData.add(newCard);
        this.entityID = JoinEntityIDs(cardData, card -> card.ID);
        return this;
    }

    @Override
    public String GetText()
    {
        String joinedString = PCLJUtils.JoinStrings(" ", PCLJUtils.Map(cardData, card -> card.Strings.NAME));
        return PGR.PCL.Strings.Actions.Obtain(joinedString, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (PCLCardData cd : cardData) {
            for (int i = 0; i < amount; i++) {
                AbstractCard c = cd.MakeCopy(false);
                for (int j = 0; j < upgradeTimes; j++) {
                    c.upgrade();
                }
                PCLActions.Bottom.MakeCardInHand(c);
            }
        }

    }
}
