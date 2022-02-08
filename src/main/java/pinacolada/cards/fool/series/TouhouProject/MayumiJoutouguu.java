package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Haniwa;
import pinacolada.utilities.PCLActions;

public class MayumiJoutouguu extends FoolCard
{
    public static final PCLCardData DATA = Register(MayumiJoutouguu.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Haniwa(), false));

    public MayumiJoutouguu()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 3);

        SetCooldown(1, 0, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);

        if (info.CanActivateSemiLimited && TrySpendAffinity(PCLAffinity.Orange) && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        for (int i=0; i<2; i++)
        {
            PCLActions.Bottom.MakeCardInDrawPile(new Haniwa());
        }
    }
}