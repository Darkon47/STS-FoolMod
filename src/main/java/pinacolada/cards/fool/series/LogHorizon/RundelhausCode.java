package pinacolada.cards.fool.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_ChannelOrb;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashSet;

public class RundelhausCode extends FoolCard
{
    public static final PCLCardData DATA = Register(RundelhausCode.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Electric, PCLCardTarget.Normal)
            .SetTraits(PCLCardTrait.Spellcaster)
            .SetSeries(CardSeries.LogHorizon);

    private static HashSet<AbstractCard> buffs;
    private static final CardEffectChoice choices = new CardEffectChoice();

    public RundelhausCode()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.General, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.LIGHTNING);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, new HashSet<>());

        PCLActions.Bottom.SelectFromHand(name, magicNumber, true)
        .SetOptions(true, false, true)
        .SetMessage(PGR.PCL.Strings.HandSelection.GenericBuff)
        .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && !buffs.contains(c) && (c.baseDamage >= 0 || c.baseBlock >= 0))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                ((PCLCard)c).AddScaling(PCLAffinity.Blue, 2);
                buffs.add(c);
                c.flash();
            }
        });

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            if (choices.TryInitialize(this))
            {
                for (PCLOrbHelper orb : PCLOrbHelper.ALL.values()) {
                    if (PCLGameUtilities.IsCommonOrb(orb.ID)) {
                        choices.AddEffect(new BaseEffect_ChannelOrb(1, orb));
                    }
                }
            }

            choices.Select(PCLActions.Bottom, 1, null)
            .CancellableFromPlayer(true);
        });
    }
}