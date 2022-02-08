package pinacolada.cards.fool.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.special.CreateRandomGoblins;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.series.GoblinSlayer.GoblinSlayer;
import pinacolada.utilities.PCLActions;

public class GoblinKing extends FoolCard
{
    public static final PCLCardData DATA = Register(GoblinKing.class)
            .SetStatus(1, CardRarity.RARE, PCLCardTarget.None, true)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinKing()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.Add(new CreateRandomGoblins(3));
        }
    }
}