package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class ReimuHakurei extends FoolCard
{
    public static final PCLCardData DATA = Register(ReimuHakurei.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public ReimuHakurei()
    {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(2, 0, 0, 1);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);

        SetProtagonist(true);

        SetAffinityRequirement(PCLAffinity.Light, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = TrySpendAffinity(PCLAffinity.Light) ? magicNumber + 1 : magicNumber;

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.ApplyVulnerable(p, m, amount);
        PCLActions.Bottom.ApplyWeak(p, m, amount);
        if (IsStarter())
        {
            PCLActions.Bottom.Scry(secondaryValue);
        }
    }
}

