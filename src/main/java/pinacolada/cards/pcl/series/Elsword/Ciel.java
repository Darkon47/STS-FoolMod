package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ciel extends PCLCard
{
    public static final PCLCardData DATA = Register(Ciel.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Lu(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Ciel()
    {
        super(DATA);

        Initialize(0, 5, 6, 3);
        SetUpgrade(0, 2, 1, 0);
        SetHitCount(2);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Blue, 5);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(hitCount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < hitCount; i++) {
            PCLActions.Bottom.GainBlock(block);
        }

        PCLActions.Bottom.ApplyLockOn(p,m,secondaryValue);

        PCLActions.Bottom.ModifyAllCopies(Lu.DATA.ID)
        .AddCallback(c ->
        {
            PCLGameUtilities.IncreaseDamage(c, magicNumber, false);
            c.flash();
        });

        if (TrySpendAffinity(PCLAffinity.Blue))
        {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(GenericEffect.EnterStance(PCLStanceHelper.WisdomStance));
                choices.AddEffect(GenericEffect.EnterStance(PCLStanceHelper.DesecrationStance));
            }
            choices.Select(1, m);
        }
    }
}