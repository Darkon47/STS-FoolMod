package pinacolada.cards.fool.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Berserker extends FoolCard
{
    public static final PCLCardData DATA = Register(Berserker.class)
            .SetAttack(3, CardRarity.COMMON, PCLAttackType.Brutal)
            .SetSeriesFromClassPackage();

    public Berserker()
    {
        super(DATA);

        Initialize(19, 0, 3, 12);
        SetUpgrade(5, 0, 0, 2);

        SetAffinity_Red(1, 0, 6);
        SetAffinity_Dark(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Red, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.VerticalImpact(m.hb));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
        .AddCallback(m.currentBlock, (initialBlock, target) ->
        {
            if (PCLGameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
            {
                PCLActions.Bottom.GainBlock(this.secondaryValue);
            }
        }));
        PCLActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
        }
    }
}