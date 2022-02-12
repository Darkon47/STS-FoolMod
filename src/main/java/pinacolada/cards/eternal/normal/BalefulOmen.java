package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class BalefulOmen extends EternalCard
{
    public static final PCLCardData DATA = Register(BalefulOmen.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.AoE);

    public BalefulOmen()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        SetUpgrade(0, 0, 0, 1);

        SetDark();
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyPower(TargetHelper.Enemies(), PCLPowerHelper.Vulnerable, secondaryValue);
        PCLActions.Bottom.ApplyPower(TargetHelper.Enemies(), PCLPowerHelper.Weak, secondaryValue);
        if (!CheckPrimaryCondition(true)) {
            PCLActions.Bottom.TakeDamage(magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }
}