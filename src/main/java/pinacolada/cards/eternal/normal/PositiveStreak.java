package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;

public class PositiveStreak extends EternalCard
{
    public static final PCLCardData DATA = Register(PositiveStreak.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None);

    public PositiveStreak()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);

        SetLight();
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < (magicNumber); i++) {
            PCLActions.Bottom.RerollAffinity(i, PCLAffinity.Light);
        }
        if (info.IsSynergizing && info.TryActivateSemiLimited()) {
            if (costForTurn > 0) {
                PCLActions.Bottom.GainEnergy(costForTurn);
            }
            PCLActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy())
                    .SetDestination(CardSelection.Top);
        }

    }
}