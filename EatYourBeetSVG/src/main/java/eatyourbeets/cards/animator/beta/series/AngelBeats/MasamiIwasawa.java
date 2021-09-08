package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class MasamiIwasawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MasamiIwasawa.class).SetSkill(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 11, 2, 3);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.MakeCardInDrawPile(new Dazed())
                .SetDestination(CardSelection.Top)
                .Repeat(secondaryValue);

        if (IsStarter())
        {
            GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        }

        if (isSynergizing) {
            GameActions.Bottom.DrawNextTurn(magicNumber);
        }
    }
}