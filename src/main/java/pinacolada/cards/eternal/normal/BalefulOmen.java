package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class BalefulOmen extends EternalCard
{
    public static final PCLCardData DATA = Register(BalefulOmen.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.AoE);

    public BalefulOmen()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);

        SetDark();
        SetEthereal(true);
        SetFragile(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        SetFragile(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        TargetHelper t = IsMismatching(info) ? TargetHelper.AllCharacters() : TargetHelper.Enemies();
        PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }
}