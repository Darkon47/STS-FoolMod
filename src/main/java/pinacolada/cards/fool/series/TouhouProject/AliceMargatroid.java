package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import pinacolada.powers.FoolPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class AliceMargatroid extends FoolCard
{
    public static final PCLCardData DATA = Register(AliceMargatroid.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public AliceMargatroid()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1,0,0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainVitality(magicNumber);
        PCLActions.Bottom.StackPower(new AlicePower(p, magicNumber, secondaryValue));
    }

    public static class AlicePower extends FoolPower implements OnTrySpendAffinitySubscriber
    {
        private int secondaryValue;

        public AlicePower(AbstractCreature owner, int amount, int secondaryValue)
        {
            super(owner, AliceMargatroid.DATA);
            this.amount = amount;
            this.secondaryValue = secondaryValue;
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            PCLCombatStats.onTrySpendAffinity.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLCombatStats.onTrySpendAffinity.Unsubscribe(this);
        }


        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();

            PCLActions.Bottom.Draw(amount);
            PCLActions.Bottom.SelectFromHand(name, amount, false)
                    .SetMessage(PGR.PCL.Strings.HandSelection.MoveToDrawPile)
                    .AddCallback(selected ->
                    {
                        for (AbstractCard c : selected) {
                            PCLActions.Top.MoveCard(c, player.hand, player.drawPile);
                        }
                    });
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, secondaryValue);
        }

        @Override
        public int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending) {
            if (isActuallySpending) {
                PCLActions.Bottom.GainBlock(secondaryValue);
            }
            return amount;
        }
    }
}
