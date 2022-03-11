package pinacolada.cards.fool.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.resources.PGR.Enums.CardTags.HASTE_INFINITE;

public class MorganLeFay extends FoolCard
{
    public static final PCLCardData DATA = Register(MorganLeFay.class)
            .SetSkill(1, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage(true);

    private static final CardEffectChoice choices = new CardEffectChoice();

    public MorganLeFay()
    {
        super(DATA);

        Initialize(0, 3, 10, 2);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) + 1;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        
        PCLActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), GetXValue());
        TrySpendAffinity(PCLAffinity.Light, GetXValue());
        if (info.TryActivateLimited()) {
            PCLActions.Bottom.Add(new CreateRandomCurses(1, p.hand)).AddCallback(card -> {
                PCLActions.Bottom.ModifyTag(card, HASTE_INFINITE, true);
                PCLActions.Bottom.ApplyPower(new MorganLeFayPower(player, card, magicNumber));
                PCLActions.Bottom.ApplyShackles(TargetHelper.AllCharacters(), magicNumber).IgnoreArtifact(true);
            });
        }
    }

    public static class MorganLeFayPower extends FoolPower
    {
        public AbstractCard card;

        public MorganLeFayPower(AbstractPlayer owner, AbstractCard card, int amount)
        {
            super(owner, MorganLeFay.DATA);

            Initialize(amount);
            this.card = card;
            updateDescription();
        }

        @Override
        public void onCardDraw(AbstractCard card)
        {
            super.onCardDraw(card);

            if (this.card == card)
            {
                PCLActions.Bottom.ApplyShackles(TargetHelper.AllCharacters(), amount).IgnoreArtifact(true);
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, card != null ? PCLJUtils.ModifyString(card.name, w -> "#y" + w) : "", amount);
        }
    }
}