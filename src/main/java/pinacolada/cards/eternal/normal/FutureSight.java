package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class FutureSight extends EternalCard
{
    public static final PCLCardData DATA = Register(FutureSight.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None);

    public FutureSight()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Scry(magicNumber).AddCallback(cards -> {
           if (PCLJUtils.Any(cards, PCLGameUtilities::IsHindrance)) {
               PCLActions.Bottom.GainEnergyNextTurn(secondaryValue);
           }
        });
    }
}