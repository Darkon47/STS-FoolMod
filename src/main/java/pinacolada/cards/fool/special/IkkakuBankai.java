package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IkkakuBankai extends FoolCard
{
    public static final PCLCardData DATA = Register(IkkakuBankai.class).SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.AoE).SetSeries(CardSeries.Bleach);

    public IkkakuBankai()
    {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(2, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1, 0, 2);
        SetExhaust(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetExhaust(!PCLGameUtilities.IsPCLAffinityPowerActive(PCLAffinity.Red));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);

        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                PCLActions.Top.DealCardDamageToAll(this, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                        .SetVFX(false, true));
            }
        });
    }
}