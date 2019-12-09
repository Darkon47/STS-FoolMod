package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorCard
{
    public static final String ID = Register(ElricAlphonse.class.getSimpleName(), EYBCardBadge.Exhaust);

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new ElricAlphonseAlt(), true);
        }
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new ElricAlphonseAlt(), upgraded, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (GameUtilities.GetPowerAmount(p, IntellectPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainIntellect(1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}