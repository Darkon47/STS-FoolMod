package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;

public class TestCard extends PCLCard
{
    public static final PCLCardData DATA = Register(TestCard.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.Normal)
            .SetColorless();

    public TestCard()
    {
        super(DATA);

        Initialize(4, 1, 2, 2);
        SetUpgrade(2, 1, 0);
        SetAffinity_Green(1, 0 ,1);
        SetAffinity_Light(1, 0, 0);

        AddDamageEffect(AttackEffects.BLUNT_LIGHT);
        AddUseEffect(BaseCondition.Starter().SetChildEffect(BaseEffect.ChannelOrb(1, PCLOrbHelper.Fire)));
        AddUseEffect(BaseCondition.Match().SetChildEffect(BaseEffect.ApplyToSingle(this, BaseEffect.PCLCardValueSource.MagicNumber, PCLPowerHelper.LockOn)));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}