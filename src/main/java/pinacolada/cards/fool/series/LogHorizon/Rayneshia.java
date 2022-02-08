package pinacolada.cards.fool.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class Rayneshia extends FoolCard
{
    public static final PCLCardData DATA = Register(Rayneshia.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Rayneshia()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);
        SetAffinityRequirement(PCLAffinity.General, 5);
    }

    @Override
    public void OnUpgrade() {
        SetHaste(true);
        SetAffinityRequirement(PCLAffinity.General, 4);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(magicNumber);
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetMessage(PGR.PCL.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(selected ->
        {
            for (AbstractCard c : selected)
            {
                PCLActions.Top.MoveCard(c, player.hand, player.drawPile).SetDestination(CardSelection.Top);
            }

            if (IsStarter()) {
                PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                    CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c : selected)
                    {
                        cardGroup.addToBottom(c);
                    }
                    PCLActions.Bottom.SelectFromPile(name, 1, cardGroup).AddCallback(cards -> {
                        if (cards.size() > 0) {
                            PCLActions.Bottom.Motivate(cards.get(0),1);
                        }
                    });

                });
            }
        });
    }
}