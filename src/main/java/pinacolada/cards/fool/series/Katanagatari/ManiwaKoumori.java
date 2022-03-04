package pinacolada.cards.fool.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.ThrowingKnife;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class ManiwaKoumori extends FoolCard
{
    public static final PCLCardData DATA = Register(ManiwaKoumori.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.Katanagatari).PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public ManiwaKoumori()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 2, 0);

        SetAffinity_Green(1);
        SetAffinity_Dark(1, 0, 2);

        SetRightHitCount(2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(1 + PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Green, true));

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.CreateThrowingKnives(secondaryValue);
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() -> PCLActions.Bottom.DiscardFromHand(name, EnergyPanel.getCurrentEnergy() * 2, false)
                                                            .SetOptions(false, false, false));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Dark) >= 10;
    }
}