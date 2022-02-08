package pinacolada.cards.fool.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.common.ImpairedPower;
import pinacolada.utilities.PCLActions;

public class Curse_Slumber extends FoolCard
{
    public static final PCLCardData DATA = Register(Curse_Slumber.class)
            .SetCurse(-2, PCLCardTarget.None, false, true)
            .SetSeries(CardSeries.NoGameNoLife);

    public Curse_Slumber()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);

        SetAffinity_Blue(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.GainFocus(secondaryValue, true);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.StackPower(new ImpairedPower(player, magicNumber, true));
        }
    }
}