package pinacolada.cards.fool.tokens;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class AffinityToken_Star extends AffinityToken
{
    public static final PCLCardData DATA = Register(AffinityToken_Star.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetColorless()
            .PostInitialize(data ->
            {
                for (PCLCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });;
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Star;

    public AffinityToken_Star()
    {
        super(DATA, AFFINITY_TYPE);
        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng, upgraded);
        PCLGameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(m, (enemy, cards) ->
                {
                    for (AbstractCard c : cards)
                    {
                        PCLActions.Bottom.PlayCopy(c, enemy);
                    }
                }));
    }

    @Override
    public int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending) {
        if (isActuallySpending && player.hand.contains(this)) {
            PCLCombatStats.onTrySpendAffinity.Unsubscribe(this);
            PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            PCLActions.Last.Purge(this).ShowEffect(true);
            return 0;
        }
        return amount;
    }
}