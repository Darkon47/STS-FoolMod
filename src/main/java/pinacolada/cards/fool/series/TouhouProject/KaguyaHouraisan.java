package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;
import java.util.List;

public class KaguyaHouraisan extends FoolCard
{
    public static final PCLCardData DATA = Register(KaguyaHouraisan.class)
            .SetSkill(1,CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (PCLCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });

    public KaguyaHouraisan()
    {
        super(DATA);

        Initialize(0, 0, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1);
        SetAffinity_Silver(1, 0, 0);
        SetCostUpgrade(-1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.isEmpty()) {

            List<AbstractCard> cardsToPlay = new ArrayList<>(player.hand.group);

            for (AbstractCard card : cardsToPlay)
            {
                PCLActions.Bottom.PlayCard(card, player.hand, null)
                        .SpendEnergy(false);
                PCLActions.Last.Exhaust(card);
                PCLActions.Last.Add(AffinityToken.SelectTokenAction(name, 1)
                        .SetOptions(true, false)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                PCLActions.Bottom.MakeCardInDrawPile(c);
                            }
                        }));
            }
        }
        else {
            PCLActions.Bottom.Scry(magicNumber);
        }

    }
}

