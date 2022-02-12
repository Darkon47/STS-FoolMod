package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class WideLeap extends EternalCard
{
    public static final PCLCardData DATA = Register(WideLeap.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None);

    public WideLeap()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .AddCallback(cards -> {
                   if (cards.size() >= magicNumber) {
                       PCLActions.Bottom.DrawNextTurn(secondaryValue);
                   }
                   if (CheckPrimaryCondition(true)) {
                       for (AbstractCard c : cards) {
                           c.freeToPlayOnce = true;
                       }
                   }
                });
    }
}