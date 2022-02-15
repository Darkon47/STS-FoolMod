package pinacolada.cards.fool.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public abstract class ImprovedDefend extends ImprovedBasicCard
{
    public static final ArrayList<PCLCardData> list = new ArrayList<>();

    public static ArrayList<PCLCardData> GetCards()
    {
        if (list.isEmpty())
        {
            list.add(Defend_Red.DATA);
            list.add(Defend_Green.DATA);
            list.add(Defend_Blue.DATA);
            list.add(Defend_Orange.DATA);
            list.add(Defend_Light.DATA);
            list.add(Defend_Dark.DATA);
            list.add(Defend_Silver.DATA);
        }

        return list;
    }

    protected static PCLCardData Register(Class<? extends PCLCard> type)
    {
        return FoolCard.Register(type).SetColorless().SetSkill(1, CardRarity.BASIC, PCLCardTarget.None)
                .SetImagePath(PGR.GetCardImage(Defend.DATA.ID + "Alt1"));
    }

    public ImprovedDefend(PCLCardData data, PCLAffinity affinity)
    {
        super(data, affinity, PGR.GetCardImage(Defend.DATA.ID + "Alt2"));

        if (affinity == PCLAffinity.Star)
        {
            Initialize(0, 5, 3);
            SetUpgrade(0, 3);
        }
        else
        {
            Initialize(0, 6, 2);
            SetUpgrade(0, 3);
        }

        SetTag(CardTags.STARTER_DEFEND, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }
}