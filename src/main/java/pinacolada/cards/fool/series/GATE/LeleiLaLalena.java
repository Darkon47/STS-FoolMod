package pinacolada.cards.fool.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class LeleiLaLalena extends FoolCard
{
    public static final PCLCardData DATA = Register(LeleiLaLalena.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public LeleiLaLalena()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && HasSynergy())
        {
            PCLGameUtilities.GetPCLIntent(m).AddFreezing();
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        target = HasSynergy() ? CardTarget.SELF_AND_ENEMY : CardTarget.SELF;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        if (info.IsSynergizing)
        {
            if (m == null)
            {
                m = PCLGameUtilities.GetRandomEnemy(true);
            }

            PCLActions.Bottom.ApplyFreezing(p, m, secondaryValue);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() -> PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Frost, magicNumber));
    }
}