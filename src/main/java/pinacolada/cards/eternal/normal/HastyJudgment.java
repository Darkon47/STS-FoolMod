package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class HastyJudgment extends EternalCard
{
    public static final PCLCardData DATA = Register(HastyJudgment.class).SetSkill(0, CardRarity.COMMON, PCLCardTarget.None);

    public HastyJudgment()
    {
        super(DATA);

        Initialize(0, 2, 0, 0);
        SetUpgrade(0, 0, 0, 0);

        SetLight();
        SetEthereal(true);
    }

    public void OnUpgrade() {
        SetHaste(true);
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        for (int i = 0; i < PCLCombatStats.MatchingSystem.ResolveMeter.Next.size(); i++) {
            PCLActions.Last.RerollAffinity(i, PCLAffinity.Light, PCLAffinity.Dark);
        }

        if (CheckPrimaryCondition(true) && EnergyPanel.getCurrentEnergy() > 0) {
            AbstractDungeon.player.energy.use(1);
            PCLActions.Bottom.Draw(1);
        }
    }
}