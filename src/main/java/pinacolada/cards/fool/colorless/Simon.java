package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class Simon extends FoolCard
{
    public static final PCLCardData DATA = Register(Simon.class).SetAttack(1, CardRarity.UNCOMMON)
            .SetColorless(PGR.Enums.Cards.THE_FOOL)
            .SetSeries(CardSeries.GurrenLagann)
            .SetTraits(PCLCardTrait.Protagonist);

    public Simon()
    {
        super(DATA);

        Initialize(5, 0, 12 , 0);
        SetUpgrade(1, 0, 0 , 0);

        SetAffinity_Red(1, 0, 1);

        SetExhaust(true);
        SetUnique(true, -1);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 5 == 0)
        {
            this.AddScaling(PCLAffinity.Red, 1);
        }

        upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH).forEach(d -> d.AddCallback(e -> {
            PCLActions.Bottom.StackPower(new VigorPower(player, e.lastDamageTaken / 2));
        }));

        if (PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Red) >= magicNumber && CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
        }
    }
}