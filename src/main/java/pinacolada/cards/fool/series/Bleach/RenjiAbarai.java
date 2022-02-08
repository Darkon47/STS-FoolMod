package pinacolada.cards.fool.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.MightStance;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class RenjiAbarai extends FoolCard
{
    public static final PCLCardData DATA = Register(RenjiAbarai.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, PCLCardTarget.AoE).SetSeriesFromClassPackage();

    public RenjiAbarai()
    {
        super(DATA);

        Initialize(10, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Red(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Green, 4);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        if (CheckAffinity(PCLAffinity.Red) && CheckAffinity(PCLAffinity.Green))
        {
            SetAttackType(PCLAttackType.Piercing);
        }
        else
        {
            SetAttackType(PCLAttackType.Normal);
        }

        SetEvokeOrbCount(HasSynergy() ? 1 : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (!VelocityStance.IsActive() || !MightStance.IsActive()){
            PCLActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
        }
    }
}