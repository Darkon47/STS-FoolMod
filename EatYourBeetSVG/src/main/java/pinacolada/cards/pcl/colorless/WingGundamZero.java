package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.IonizingStorm;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class WingGundamZero extends PCLCard
{
    public static final PCLCardData DATA = Register(WingGundamZero.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.Self).SetColor(CardColor.COLORLESS)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.Gundam);

    public WingGundamZero()
    {
        super(DATA);

        Initialize(0, 5, 7, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Silver(1, 0, 2);
        SetAffinity_Light(1, 0, 2);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Silver, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup[] groups = upgraded ? (new CardGroup[] {p.discardPile, p.drawPile, p.hand}) : (new CardGroup[] {p.hand});
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SelectFromPile(name, 999, groups)
                .SetOptions(false, false)
                .AddCallback((cards) -> {
                   for (AbstractCard c : cards) {
                       PCLCard pC = PCLJUtils.SafeCast(c, PCLCard.class);
                       c.upgrade();
                       if (pC != null && pC.maxUpgradeLevel > 0) {
                           pC.maxUpgradeLevel += 1;
                       }
                   }
                    if (cards.size() < secondaryValue && info.TryActivateLimited()) {
                        AbstractCard c = new IonizingStorm();
                        c.applyPowers();
                        PCLActions.Bottom.PlayCopy(c, null);
                    }
                });
    }
}