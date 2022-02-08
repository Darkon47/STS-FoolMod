package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.replacement.PCLWeakPower;
import pinacolada.utilities.PCLActions;

public class Curse_Doubt extends PCLCard
{
    public static final PCLCardData DATA = Register(Curse_Doubt.class)
            .SetCurse(-2, PCLCardTarget.None, false, true);

    public Curse_Doubt()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Top.ReducePower(player, player, FrailPower.POWER_ID, secondaryValue);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.StackPower(new PCLWeakPower(player, magicNumber, true));
        }
    }
}