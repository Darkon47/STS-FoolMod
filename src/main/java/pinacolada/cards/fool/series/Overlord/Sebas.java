package pinacolada.cards.fool.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.stances.pcl.EnduranceStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Sebas extends FoolCard
{
    public static final PCLCardData DATA = Register(Sebas.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Sebas()
    {
        super(DATA);

        Initialize(0, 9, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Red, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        final int counter = PCLJUtils.Count(PCLGameUtilities.GetPCLIntents(), PCLEnemyIntent::IsAttacking);
        if (counter > 0)
        {
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, counter * magicNumber));
        }

        int energy = 0;
        if (EnduranceStance.IsActive())
        {
            energy += 1;
        }
        if (TrySpendAffinity(PCLAffinity.Red))
        {
            energy += 1;
        }

        PCLActions.Bottom.GainEnergy(energy);
    }
}