package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Rayneshia extends AnimatorCard {
    public static final EYBCardData DATA = Register(Rayneshia.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    protected static final ArrayList<AbstractCard> synergicCards = new ArrayList();

    public Rayneshia() {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AddCardsFromGroupToSynergy(player.discardPile);
        DrawSynergicCards();
    }

    @Override
    public boolean HasSynergy(AbstractCard other)
    {
        return (other.cost == 2) || other.hasTag(SPELLCASTER) || super.HasSynergy(other);
    }

    private void AddCardsFromGroupToSynergy(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Synergies.WouldSynergize(this, c))
            {
                synergicCards.add(c);
            }
        }
    }

    private void DrawSynergicCards()
    {
        RandomizedList<AbstractCard> randomizedSynergicCards = new RandomizedList<>(synergicCards);

        for (int i=0; i<magicNumber; i++)
        {
            if (i > randomizedSynergicCards.Size())
            {
                break;
            }

            AbstractCard randomCard = randomizedSynergicCards.Retrieve(rng, true);

            if (randomCard != null)
            {
                GameActions.Top.MoveCard(randomCard, player.discardPile, player.hand)
                        .ShowEffect(true, true);
            }
        }
    }
}