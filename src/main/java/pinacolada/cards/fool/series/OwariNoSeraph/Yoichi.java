package pinacolada.cards.fool.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Yoichi extends FoolCard
{
    public static final PCLCardData DATA = Register(Yoichi.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,1, 1);
        SetUpgrade(0,1, 1);

        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, 1, false);
        PCLActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber))
        .AddCallback(power ->
        {
            if (info.IsSynergizing && info.TryActivateSemiLimited())
            {
                SupportDamagePower supportDamage = PCLJUtils.SafeCast(power, SupportDamagePower.class);
                if (supportDamage != null)
                {
                    supportDamage.atEndOfTurn(true);
                }
            }
        });
    }
}