package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class AllPreserver extends EternalCard
{
    public static final PCLCardData DATA = Register(AllPreserver.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None);

    public AllPreserver()
    {
        super(DATA);

        Initialize(0, 1, 2, 0);
        SetUpgrade(0, 3, 0, 0);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.RetainFromHand(name, magicNumber, false)
                .AddCallback(cards -> {
                   if (CheckPrimaryCondition(true)) {
                       for (AbstractCard c : cards) {
                           PCLActions.Bottom.ModifyTag(c, PCL_RETAIN_INFINITE, true);
                       }
                       PCLActions.Last.Exhaust(this);
                   }
                });
    }
}