package pinacolada.cards.fool.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.pcl.status.Status_Dazed;
import pinacolada.utilities.PCLActions;

public class MasamiIwasawa extends FoolCard
{
    public static final PCLCardData DATA = Register(MasamiIwasawa.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Dazed(), false));

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 12, 1, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetEthereal(true);
        SetAfterlife(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        

        PCLActions.Bottom.MakeCardInDrawPile(new Status_Dazed())
                .Repeat(secondaryValue);

        if (IsStarter())
        {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void triggerOnAfterlife() {
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
    }
}