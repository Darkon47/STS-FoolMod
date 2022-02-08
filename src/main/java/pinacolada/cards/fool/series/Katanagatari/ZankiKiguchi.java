package pinacolada.cards.fool.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class ZankiKiguchi extends FoolCard
{
    public static final PCLCardData DATA = Register(ZankiKiguchi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public ZankiKiguchi()
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(1, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (!info.IsSynergizing) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(PCLAffinity.Green)
                    .SetOptions(true, true);
        }

        if (info.TryActivateSemiLimited()) {
            PCLActions.Bottom.Cycle(name, magicNumber);
        }
    }
}