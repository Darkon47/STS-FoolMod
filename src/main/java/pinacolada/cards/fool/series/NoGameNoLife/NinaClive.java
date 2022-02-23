package pinacolada.cards.fool.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

import static eatyourbeets.resources.GR.Enums.CardTags.DELAYED;
import static pinacolada.resources.PGR.Enums.CardTags.PCL_INNATE;

public class NinaClive extends FoolCard
{
    public static final PCLCardData DATA = Register(NinaClive.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public NinaClive()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0,0,0);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 1) {
            if (form == 1) {
                Initialize(0, 0, 2);
                SetUpgrade(0,0,1);
            }
            else {
                SetRetain(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.hand, player.discardPile)
        .SetOptions(false, true)
        .SetMessage(PGR.PCL.Strings.GridSelection.Give(magicNumber, PGR.Tooltips.Delayed.title))
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards) {
                PCLActions.Bottom.ModifyTag(c,DELAYED,true);
            }

            if (cards.size() > 0) {
                PCLActions.Bottom.SelectFromPile(name, cards.size(), player.hand, player.discardPile)
                        .SetOptions(false, true)
                        .SetMessage(PGR.PCL.Strings.GridSelection.Give(cards.size(), PGR.Tooltips.Innate.title))
                        .AddCallback(cards2 -> {
                            for (AbstractCard c2 : cards2) {
                                PCLActions.Bottom.ModifyTag(c2, PCL_INNATE,true);
                            }
                        });
            }
        });

        PCLActions.Last.ReshuffleDiscardPile(false);
    }


}