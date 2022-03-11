package pinacolada.cards.fool.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.ElricAlphonse_Alt;
import pinacolada.ui.combat.FoolAffinityMeter;
import pinacolada.utilities.PCLActions;

public class ElricAlphonse extends FoolCard
{
    public static final PCLCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ElricAlphonse_Alt(), true));

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 2, 6, 1);
        SetUpgrade(0, 1, 4);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.MakeCardInDiscardPile(new ElricAlphonse_Alt()).SetUpgrade(upgraded, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.RerollAffinity(FoolAffinityMeter.TARGET_CURRENT, PCLAffinity.Blue, PCLAffinity.Orange)
                .SetOptions(false, true);
    }
}