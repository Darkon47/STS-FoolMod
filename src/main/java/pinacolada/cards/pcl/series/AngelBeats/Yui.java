package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.GirlDeMo;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Yui extends PCLCard
{
    public static final PCLCardData DATA = Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.Self, true).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new GirlDeMo(), false));
    public static final int CAP = 5;

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 7, 2);
        SetAffinity_Star(1, 0, 0);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinityRequirement(PCLAffinity.General, 6);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(CAP);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return Math.min(CAP, PCLGameUtilities.GetCurrentMatchCombo() / secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCard(PCLAffinity.Light)).SetUpgrade(upgraded, false).AddCallback(
                () -> {
                    PCLActions.Bottom.Motivate(GetXValue());
                }
        );
    }

    @Override
    public void triggerOnAfterlife() {
        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}