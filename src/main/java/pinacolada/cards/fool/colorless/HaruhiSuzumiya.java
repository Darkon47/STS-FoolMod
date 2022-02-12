package pinacolada.cards.fool.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ReactivePower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.SFX;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class HaruhiSuzumiya extends FoolCard
{
    public static final PCLCardData DATA = Register(HaruhiSuzumiya.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HaruhiSuzumiya);
    private CardType cardType;

    public HaruhiSuzumiya()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetCostUpgrade(-1);
        SetEthereal(true);
        SetPurge(true);

        SetAffinity_Star(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.General, 3);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        HashMap<AbstractCard, PCLRuntimeLoadout> rMap = new HashMap<>();
        RandomizedList<AbstractCard> seriesCards = new RandomizedList<>();
        for (PCLRuntimeLoadout loadout : PGR.PCL.Dungeon.Loadouts) {
            AbstractCard mapCard = new PCLCardBuilder(String.valueOf(loadout.ID))
                    .SetImagePath(loadout.Loadout.GetSymbolicCard().CreateNewInstance().assetUrl)
                    .ShowTypeText(false)
                    .SetText(loadout.Loadout.Name, PGR.PCL.Strings.SeriesSelection.ContainsNCards(loadout.GetCardPoolInPlay().size()), null)
                    .CanUpgrade(false)
                    .Build();
            seriesCards.Add(mapCard);
            rMap.put(mapCard, loadout);
        }
        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (cardGroup.size() < magicNumber && seriesCards.Size() > 0) {
            cardGroup.addToTop(seriesCards.Retrieve(rng));
        }
        PCLActions.Bottom.SelectFromPile(name, 1, cardGroup).AddCallback(series -> {
            if (series.size() > 0) {
                PCLRuntimeLoadout loadout = rMap.get(series.get(0));
                if (loadout != null) {
                    PCLActions.Bottom.PurgeFromPile(name, 9999, p.hand, p.discardPile, p.drawPile)
                            .SetOptions(true, true)
                            .AddCallback(cards -> {
                                for (AbstractCard c : CreateDeck(loadout, cards.size())) {
                                    PCLActions.Bottom.MakeCardInDrawPile(c);
                                }
                                PCLActions.Bottom.Draw(secondaryValue).AddCallback(cards2 -> {
                                    for (AbstractCard c : cards2) {
                                        PCLActions.Bottom.Motivate(c, 1);
                                    }
                                });
                            });
                }

            }

        });

        PCLActions.Bottom.TryChooseSpendAffinity(this).CancellableFromPlayer(true)
                .AddConditionalCallback(() -> {
                    PCLActions.Bottom.VFX(new BorderFlashEffect(Color.YELLOW));
                    PCLActions.Bottom.SFX(SFX.MONSTER_COLLECTOR_DEBUFF,1.5f,1.5f);
                   for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                       PCLActions.Bottom.ApplyPower(new ReactivePower(mo));
                   }
                });
    }

    protected ArrayList<AbstractCard> CreateDeck(PCLRuntimeLoadout loadout, int size) {
        WeightedList<AbstractCard> rewards = new WeightedList<>();

        for (AbstractCard c : loadout.GetCardPoolInPlay().values())
        {
            if (!PCLGameUtilities.IsHindrance(c) && PCLGameUtilities.IsObtainableInCombat(c))
            {
                switch (c.rarity)
                {
                    case COMMON:
                        rewards.Add(c, 40);
                        break;

                    case UNCOMMON:
                        rewards.Add(c, 40);
                        break;

                    case RARE:
                        rewards.Add(c, 20);
                        break;
                }
            }
        }

        ArrayList<AbstractCard> results = new ArrayList<>();
        while (results.size() < size && rewards.Size() > 0) {
            PCLCard r = PCLJUtils.SafeCast(rewards.Retrieve(PCLCard.rng, true), PCLCard.class);
            if (r != null && r.cardData != null) {
                results.add(r);
                int count = PCLJUtils.Count(results, c -> c.cardID.equals(r.cardID));
                if (count < r.cardData.MaxCopies) {
                    rewards.Add(r, 20 - count);
                }
            }
        }
        return results;
    }
}