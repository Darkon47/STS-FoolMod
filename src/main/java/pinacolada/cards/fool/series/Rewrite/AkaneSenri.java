package pinacolada.cards.fool.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class AkaneSenri extends FoolCard
{
    public static final PCLCardData DATA = Register(AkaneSenri.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int BLESSING_COST = 6;

    public AkaneSenri()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 0);
        SetEthereal(true);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(BLESSING_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new AkaneSenriPower(p, magicNumber, secondaryValue));
        PCLActions.Bottom.ModifyTag(player.drawPile, magicNumber, HASTE, true);
    }

    public static class AkaneSenriPower extends PCLClickablePower implements OnShuffleSubscriber
    {
        protected int secondaryAmount;

        public AkaneSenriPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, AkaneSenri.DATA, PowerTriggerConditionType.Affinity, BLESSING_COST, null, null, PCLAffinity.Light);
            this.secondaryAmount = secondaryAmount;
            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            PCLCombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLCombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            AbstractOrb darkOrb = new Dark();
            PCLActions.Bottom.ChannelOrb(darkOrb);
            PCLActions.Bottom.TriggerOrbPassive(darkOrb,player.hand.size());
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryAmount, BLESSING_COST);
        }

        @Override
        public void OnShuffle(boolean triggerRelics) {
            if (!owner.powers.contains(this))
            {
                PCLCombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            PCLActions.Bottom.ModifyTag(player.drawPile, amount, HASTE, true);
            PCLActions.Bottom.GainVelocity(secondaryAmount);
        }
    }
}