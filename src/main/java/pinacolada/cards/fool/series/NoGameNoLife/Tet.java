package pinacolada.cards.fool.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCardTrait;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class Tet extends FoolCard
{
    public static final PCLCardData DATA = Register(Tet.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetTraits(PCLCardTrait.Fairy)
            .SetSeriesFromClassPackage();

    public Tet()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, 1, player.hand, player.drawPile)
        .SetMessage(PGR.PCL.Strings.GridSelection.Discard)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                PCLActions.Top.MoveCard(card, player.discardPile);
            }
        });

        PCLActions.Bottom.SelectFromPile(name, 1, player.discardPile)
        .SetMessage(PGR.PCL.Strings.GridSelection.MoveToDrawPile(1))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                PCLActions.Top.MoveCard(card, player.drawPile)
                .SetDestination((list, c, index) ->
                {
                    index += rng.random(list.size() / 2);
                    index = Math.max(0, Math.min(list.size(), list.size() - index));
                    list.add(index, c);
                });
            }
        });
    }
}