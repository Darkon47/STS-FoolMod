package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class Leadership extends EternalCard
{
    public static final PCLCardData DATA = Register(Leadership.class).SetSkill(0, CardRarity.COMMON, PCLCardTarget.None);

    public Leadership()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);

        SetLight();
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IsStarter()) {
            PCLActions.Bottom.Motivate(magicNumber);
        }
        if (CheckPrimaryCondition(true) && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.Draw(magicNumber);
        }
    }
}