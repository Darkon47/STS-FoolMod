package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.ui.combat.FoolAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MoonlightChaser extends EternalCard
{
    public static final PCLCardData DATA = Register(MoonlightChaser.class).SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None);

    public MoonlightChaser()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);

        SetDark();
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile)
                .SetFilter(PCLGameUtilities::HasDarkAffinity);
        if (IsMismatching(info)) {
            PCLActions.Bottom.RerollAffinity(FoolAffinityMeter.TARGET_CURRENT, PCLAffinity.Dark);
        }
    }
}