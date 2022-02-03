package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;

public class Fujimaki extends PCLCard
{
    public static final PCLCardData DATA = Register(Fujimaki.class).SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Fujimaki()
    {
        super(DATA);

        Initialize(10, 0, 2);
        SetUpgrade(3, 0, 0);

        SetCooldown(1, 0, this::OnCooldownCompleted);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Green, 4);
    }

    @Override
    public void triggerOnPurge() {
        PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (TrySpendAffinity(PCLAffinity.Green))
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.GainMight(magicNumber);
        PCLActions.Bottom.MakeCardInHand(new Wound());
    }
}