package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.SwordfishII;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

import java.util.HashSet;
import java.util.Set;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STARTER_STRIKE;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STRIKE;

public class SpikeSpiegel extends FoolCard
{
    public static final PCLCardData DATA = Register(SpikeSpiegel.class).SetAttack(3, CardRarity.RARE, PCLAttackType.Ranged)
            .SetColorless()
            .SetSeries(CardSeries.CowboyBebop)
            .PostInitialize(data -> data.AddPreview(new SwordfishII(), false));

    public SpikeSpiegel()
    {
        super(DATA);

        Initialize(8, 0, 3, 3);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        PCLActions.Bottom.PlayFromPile(name, magicNumber, m, player.drawPile)
                .SetFilter(c -> c.hasTag(STRIKE) || c.hasTag(STARTER_STRIKE))
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.ModifyCost(c, -1, true, true);
                    }
                });

        if (CheckSpecialCondition(true) && CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.MakeCardInDiscardPile(new SwordfishII()).SetUpgrade(upgraded, false);
        }

    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        Set<CardType> cardTypes = new HashSet<>();
        for (AbstractCard c : player.hand.group) {
            cardTypes.add(c.type);
        }
        return cardTypes.size() >= secondaryValue;
    }
}