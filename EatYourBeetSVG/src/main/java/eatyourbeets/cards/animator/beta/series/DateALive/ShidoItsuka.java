package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class ShidoItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShidoItsuka.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    protected final static ArrayList<AbstractCard> dateALiveCards = new ArrayList<>();

    public ShidoItsuka()
    {
        super(DATA);

        Initialize(0, 7, 3);
        SetUpgrade(0, 0);
        SetAffinity_Blue(1, 0, 1);

        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        InitializeSynergicCards();
        boolean giveHarmonic = IsStarter() && info.TryActivateLimited();

        RandomizedList<AbstractCard> randomizedDALCards = new RandomizedList<>(dateALiveCards);
        final CardGroup options = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        for (int i = 0; i < magicNumber; i++)
        {
            if (randomizedDALCards.Size() == 0)
            {
                break;
            }

            AbstractCard randomCard = randomizedDALCards.Retrieve(rng, true).makeCopy();

            if (upgraded)
            {
                randomCard.upgrade();
            }
            if (giveHarmonic)
            {
                GameUtilities.ModifyCardTag(randomCard, HARMONIC, true);
            }

            options.addToBottom(randomCard);
        }

        GameActions.Top.SelectFromPile(name, 1, options)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        if (info.IsSynergizing) {
                            GameActions.Bottom.MakeCardInDrawPile(cards.get(0))
                                    .SetDuration(Settings.ACTION_DUR_FASTER, true);
                        }
                        else {
                            GameActions.Bottom.MakeCardInDiscardPile(cards.get(0))
                                    .SetDuration(Settings.ACTION_DUR_FASTER, true);
                        }

                    }
                });

        if (info.IsSynergizing && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Last.ModifyAllInstances(uuid, c -> ((EYBCard) c).SetExhaust(true));
        }
    }

    private void InitializeSynergicCards()
    {
        dateALiveCards.clear();

        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (GameUtilities.IsSameSeries(this, c)
            && !c.hasTag(AbstractCard.CardTags.HEALING)
            && c.rarity != AbstractCard.CardRarity.SPECIAL
            && c.rarity != AbstractCard.CardRarity.BASIC)
            {
                dateALiveCards.add(c);
            }
        }
    }
}