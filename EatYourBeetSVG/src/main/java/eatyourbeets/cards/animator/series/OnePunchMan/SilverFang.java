package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class SilverFang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SilverFang.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public SilverFang()
    {
        super(DATA);

        Initialize(0, 9, 1);
        SetUpgrade(0, 3, 0);

        SetSeries(CardSeries.OnePunchMan);
        SetAffinity(1, 2, 0, 2, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
            .SetFilter(c -> c instanceof EYBCard && c.type == CardType.ATTACK)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    EYBCard card = (EYBCard)cards.get(0);
                    card.agilityScaling += 1;
                    card.flash();
                }
            });
        }

        if (isSynergizing)
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }
    }
}