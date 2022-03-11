package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_ModifyAffinityScaling;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_ModifyTag;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.IonizingStorm;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class PurpleHeart extends FoolCard
{
    public static final PCLCardData DATA = Register(PurpleHeart.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.Self)
            .SetColorless()
            .SetMaxCopies(2)
            .SetSeries(CardSeries.HyperdimensionNeptunia)
            .PostInitialize(data -> {data.AddPreview(new IonizingStorm(), false);
            });
    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final RandomizedList<PCLCardTagHelper> possibleTags = new RandomizedList<>();
    private static final RandomizedList<PCLAffinity> possibleAffinities = new RandomizedList<>();
    private static HashSet<UUID> buffs;

    public PurpleHeart()
    {
        super(DATA);

        Initialize(0, 1, 2, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Silver(1, 0, 2);
        SetAffinity_Light(1, 0, 2);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Silver, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = PCLCombatStats.GetEffectCombatData(cardID, new HashSet<>());
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && !buffs.contains(c.uuid))
                .AddCallback((cards) -> {
                    if (cards == null || cards.isEmpty()) {
                        return;
                    }
                    boolean isMatching = false;
                    PCLCardAffinities caf = null;
                    for (AbstractCard c : cards) {
                        buffs.add(c.uuid);
                        PCLCardAffinities ncaf = PCLGameUtilities.GetPCLAffinities(c);
                        if (caf == null) {
                            caf = ncaf;
                        }
                        else if (ncaf != null && ncaf.HasSameAffinities(caf)) {
                            isMatching = true;
                        }
                    }

                    possibleTags.Clear();
                    possibleTags.AddAll(PCLJUtils.Filter(PCLCardTagHelper.ALL.values(), t -> t.IsBuff));
                    possibleAffinities.Clear();
                    possibleAffinities.AddAll(PCLAffinity.Extended());
                    choices.Initialize(this, true);
                    for (int i = 0; i < secondaryValue; i++) {
                        choices.AddEffect(GetRandomEffect(cards));
                    }
                    choices.Select(1, m);

                    if (isMatching && info.TryActivateLimited()) {
                        AbstractCard c = new IonizingStorm();
                        c.applyPowers();
                        PCLActions.Bottom.PlayCopy(c, null);
                    }
                });
    }

    protected BaseEffect GetRandomEffect(ArrayList<AbstractCard> cards) {
        if (rng.randomBoolean()) {
            return new BaseEffect_ModifyTag(cards, possibleTags.Retrieve(rng, true))
                    .AddTag(upgraded ? possibleTags.Retrieve(rng, true) : null);
        }
        return new BaseEffect_ModifyAffinityScaling(magicNumber, cards, possibleAffinities.Retrieve(rng, true))
                .AddAffinity(upgraded ? possibleAffinities.Retrieve(rng, true) : null);
    }
}