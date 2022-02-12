package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class OminousInscription extends EternalCard
{
    public static final PCLCardData DATA = Register(OminousInscription.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.AoE);

    public OminousInscription()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);

        SetDark();

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyWeak(info.IsSynergizing ? TargetHelper.Enemies() : TargetHelper.Player(), magicNumber);
    }
}