package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class ImperialOrder extends EternalCard
{
    public static final PCLCardData DATA = Register(ImperialOrder.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None);

    public ImperialOrder()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.drawPile.size() > 0)
        {
            AbstractCard card = player.drawPile.getTopCard();

            if (card instanceof EternalCard)
            {
                PCLActions.Top.PlayCard(card, player.drawPile, null)
                        .SpendEnergy(false);
            }
            else if (info.TryActivateSemiLimited())
            {
                PCLActions.Bottom.Cycle(name, magicNumber);
            }
        }
    }
}