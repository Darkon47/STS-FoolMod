package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.FoolClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shenhe extends FoolCard
{
    public static final PCLCardData DATA = Register(Shenhe.class).SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage(true);
    private static final int POWER_ENERGY_COST = 8;

    public Shenhe()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Green(1);
        SetAffinity_Blue(1);
        SetEthereal(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromPile(name, magicNumber, player.drawPile, player.hand, player.discardPile)
                .SetOptions(true, true)
                .SetFilter(c -> c instanceof PCLCard && ((PCLCard) c).attackType == PCLAttackType.Fire);
        PCLActions.Bottom.ApplyPower(new ShenhePower(p, this.magicNumber));
    }

    public static class ShenhePower extends FoolClickablePower
    {

        public ShenhePower(AbstractCreature owner, int amount)
        {
            super(owner, Shenhe.DATA, PowerTriggerConditionType.Affinity, POWER_ENERGY_COST, __ -> player.hand.size() > 0, null, PCLAffinity.Blue);
            this.triggerCondition.SetPayCost(__ -> {
                PCLActions.Bottom.ExhaustFromHand(name, 1, false)
                        .SetOptions(false, false, false);
            });

            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.FetchFromPile(name, 1, player.exhaustPile)
                    .SetFilter(c -> c instanceof PCLCard && (((PCLCard) c).attackType == PCLAttackType.Fire || ((PCLCard) c).attackType == PCLAttackType.Ice));
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
            if (c != null && c.attackType == PCLAttackType.Ice)
            {
                PCLActions.Bottom.ApplyFreezing(m != null ? TargetHelper.Normal(m) : TargetHelper.Enemies(), amount);
                this.flashWithoutSound();
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, POWER_ENERGY_COST);
        }
    }
}