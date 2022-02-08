package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

import static pinacolada.resources.PGR.Enums.CardTags.PCL_ETHEREAL;

public class HiedaNoAkyuu extends FoolCard
{
    public static final PCLCardData DATA = Register(HiedaNoAkyuu.class).SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage(true);

    public HiedaNoAkyuu()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
        SetCostUpgrade(-1);
        SetAffinityRequirement(PCLAffinity.General, 15);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.MoveCards(player.drawPile, player.discardPile)
        .SetDuration(0.01f, false);

        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile)
        .SetMessage(PGR.PCL.Strings.GridSelection.Give(magicNumber, PGR.Tooltips.Innate.title))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            boolean canRemoveTags = cards.size() > 0 && CheckAffinity(PCLAffinity.General) && info.TryActivateLimited();
            for (AbstractCard card : cards)
            {
                PCLActions.Bottom.ModifyTag(card, PCL_INNATE,true);
                if (canRemoveTags) {
                    PCLActions.Bottom.ModifyTag(card, PCL_ETHEREAL,false);
                    card.isEthereal = false;
                }
            }
        });

        PCLActions.Bottom.StackPower(new NoDrawPower(p));
    }
}

