package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class GreatExpectations extends EternalCard
{
    public static final PCLCardData DATA = Register(GreatExpectations.class).SetPower(3, CardRarity.RARE);

    public GreatExpectations()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 0, 0);

        SetLight();
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetInnate(true);
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() -> {
            PCLCombatStats.MatchingSystem.ResolveMeter.ResolveGain += magicNumber;
        });
    }
}