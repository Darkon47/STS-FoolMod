package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.RotatingList;

public class Vladilena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vladilena.class)
            .SetSkill(1, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.EightySix);

    public Vladilena()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0,0,1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 1, 0);
        SetAffinity_Silver(2);
        SetAffinity_Light(1);

        SetDrawPileCardPreview(this::FindCards);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
        pile.AddAll(player.discardPile.group);
        while (group.size() < magicNumber && pile.Size() > 0)
        {
            AbstractCard ca = pile.Retrieve(rng);
            if (ca != null && ca.type == CardType.ATTACK) {
                group.addToTop(ca);
            }
        }

        if (group.size() >= 1) {
            GameActions.Bottom.SelectFromPile(name, 1, group)
                    .SetOptions(false, false)
                    .SetFilter(c -> c.type == CardType.ATTACK)
                    .AddCallback(cards -> {
                        for (AbstractCard c : cards) {
                            if (c.rarity.equals(CardRarity.BASIC) && info.TryActivateLimited()) {
                                GameActions.Last.Add(AffinityToken.SelectTokenAction(name, 1, magicNumber, upgraded)
                                        .AddCallback(tokens ->
                                        {
                                            for (AbstractCard t : tokens)
                                            {
                                                GameActions.Bottom.MakeCardInHand(t);
                                            }
                                        }));
                            }
                            GameActions.Top.PlayCopy(c.makeStatEquivalentCopy(), m);
                            GameActions.Bottom.PlayCard(c, m).AddCallback(() -> {
                                GameActions.Last.Exhaust(c);
                            });
                            //GameActions.Bottom.DealDamageAtEndOfTurn(player, player, c.costForTurn * 2, AttackEffects.SLASH_VERTICAL);
                        }
                    });
        }

    }

    protected void FindCards(RotatingList<AbstractCard> cards, AbstractMonster target)
    {
        cards.Clear();
        for (AbstractCard c : player.drawPile.group)
        {
            if (IsValid(c, target))
            {
                cards.Add(c);
            }
        }
        for (AbstractCard c : player.discardPile.group)
        {
            if (IsValid(c, target))
            {
                cards.Add(c);
            }
        }
    }

    protected boolean IsValid(AbstractCard c, AbstractMonster target) {
        return c.type == CardType.ATTACK && GameUtilities.IsPlayable(c, target) && !c.tags.contains(GR.Enums.CardTags.VOLATILE);
    }
}