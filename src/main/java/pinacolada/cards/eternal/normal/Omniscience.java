package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class Omniscience extends EternalCard
{
    public static final PCLCardData DATA = Register(Omniscience.class).SetPower(1, CardRarity.UNCOMMON);

    public Omniscience()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 0, 0);

        SetLight();

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() -> {
           for (int i = 0; i < magicNumber; i++) {
               PCLCombatStats.MatchingSystem.ResolveMeter.AddQueueItem();
           }
        });
    }
}