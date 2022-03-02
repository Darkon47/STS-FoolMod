package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.replacement.PCLFrailPower;
import pinacolada.utilities.PCLActions;

public class Curse_Shame extends PCLCard
{
    public static final PCLCardData DATA = Register(Curse_Shame.class)
            .SetCurse(-2, PCLCardTarget.None, false, true);

    public Curse_Shame()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetAffinity_Dark(1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Top.ReducePower(player, player, VulnerablePower.POWER_ID, secondaryValue);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.StackPower(new PCLFrailPower(player, magicNumber, true));
        }
    }
}