package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DesperateSacrifice extends EternalCard
{
    public static final PCLCardData DATA = Register(DesperateSacrifice.class).SetAttack(1, CardRarity.RARE);

    public DesperateSacrifice()
    {
        super(DATA);

        Initialize(0, 0, 5, 3);
        SetUpgrade(0, 0);

        SetLight();
        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        boolean survive = CheckSpecialCondition(true);
        PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(magicNumber);
        PCLActions.Bottom.LoseHP(secondaryValue, AbstractGameAction.AttackEffect.NONE).CanKill(survive);
        if (survive) {
            PCLActions.Bottom.Draw(secondaryValue);
        }
    }

    public boolean CheckSpecialCondition(boolean tryUse){
        return (player.currentHealth + player.currentBlock + PCLGameUtilities.GetTempHP() < secondaryValue) && (tryUse ? CombatStats.TryActivateLimited(cardID) : CombatStats.CanActivateLimited(cardID));
    }
}