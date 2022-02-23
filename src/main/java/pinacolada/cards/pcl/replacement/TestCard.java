package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;

public class TestCard extends PCLCard
{
    public static final PCLCardData DATA = Register(TestCard.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.Normal)
            .SetColorless();

    public TestCard()
    {
        super(DATA);

        Initialize(1, 0, 2);
        SetUpgrade(0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.SWORD1, m.hb));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}