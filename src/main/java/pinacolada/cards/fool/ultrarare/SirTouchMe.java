package pinacolada.cards.fool.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class SirTouchMe extends FoolCard_UltraRare
{
    public static final PCLCardData DATA = Register(SirTouchMe.class)
            .SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Brutal)
            .SetColorless()
            .SetSeries(CardSeries.Overlord);

    public SirTouchMe()
    {
        super(DATA);

        Initialize(4, 4, 4, 3);
        SetUpgrade(2, 2, 2, 0);
        
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.StackPower(new JuggernautPower(p, magicNumber));
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.GainPlatedArmor(secondaryValue);
        }
    }
}