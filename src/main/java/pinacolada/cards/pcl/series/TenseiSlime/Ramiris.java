package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Ramiris extends PCLCard
{
    public static final PCLCardData DATA = Register(Ramiris.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.AoE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int CAP = 10;

    public Ramiris()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetHaste(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(CAP);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int GetXValue() {
        return Math.min(CAP, PCLGameUtilities.GetCurrentMatchCombo());
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
        {
            intent.AddStrength(-GetXValue());
            intent.AddBlinded();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryFocus, -secondaryValue);
        PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Shackles, GetXValue());
        PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Blinded, GetXValue());
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}