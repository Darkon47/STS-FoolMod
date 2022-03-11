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

public class PurpleHaze extends EternalCard
{
    public static final PCLCardData DATA = Register(PurpleHaze.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.Self);

    public PurpleHaze()
    {
        super(DATA);

        Initialize(0, 1, 1, 0);
        SetUpgrade(0, 3, 0, 0);

        SetDark();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.GainBlur(magicNumber);
        PCLActions.Bottom.RerollAffinity(FoolAffinityMeter.TARGET_CURRENT, PCLAffinity.Dark);
    }
}