package pinacolada.cards.fool.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ItamiYouji extends FoolCard
{
    public static final PCLCardData DATA = Register(ItamiYouji.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .SetTraits(PCLCardTrait.Protagonist);

    public ItamiYouji()
    {
        super(DATA);

        Initialize(2, 0, 3, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Silver(1);

        SetAffinityRequirement(PCLAffinity.Orange, 7);

        SetExhaust(true);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLGameUtilities.GetTotalCardsPlayed(this, true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(magicNumber).AddCallback(() -> {
            PCLActions.Top.Reload(name, m, (enemy, cards) ->
            {
                for (int i = 0; i < cards.size(); i++) {
                    if (enemy != null && !PCLGameUtilities.IsDeadOrEscaped(enemy))
                    {
                        PCLActions.Bottom.DealCardDamage(this, enemy, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(1.3f, 1.5f));
                    }
                }
            });
        });

        if (TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.GainSupportDamage(GetXValue());
        }
    }
}