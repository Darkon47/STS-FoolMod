package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.interfaces.subscribers.OnPCLClickablePowerUsed;
import pinacolada.powers.FoolClickablePower;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class KawashiroNitori extends FoolCard
{
    public static final PCLCardData DATA = Register(KawashiroNitori.class)
            .SetPower(1, CardRarity.COMMON)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage(true);

    public KawashiroNitori()
    {
        super(DATA);

        Initialize(0, 0, 2, 5);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();

        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new KawashiroNitoriPower(p, 1, magicNumber));
    }

    public static class KawashiroNitoriPower extends FoolClickablePower implements OnPCLClickablePowerUsed
    {
        protected int secondaryAmount;

        public KawashiroNitoriPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, KawashiroNitori.DATA, PowerTriggerConditionType.Affinity, 5);
            this.amount = amount;
            this.secondaryAmount = secondaryAmount;
            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onPCLClickablePowerUsed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onPCLClickablePowerUsed.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.SelectFromHand(name, 1, false)
                    .SetOptions(true, true, true)
                    .SetMessage(PGR.PCL.Strings.GridSelection.Give(amount, PGR.Tooltips.Retain.title))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards) {
                            PCLActions.Bottom.ModifyTag(c, HASTE, true);
                        }
                    });
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target) {
            if (amount > 0 && power instanceof AbstractPCLAffinityPower) {
                PCLActions.Bottom.AddAffinity(PCLJUtils.Random(PCLAffinity.Extended()), secondaryAmount);
                amount -= 1;
                flash();
            }
            return true;
        }
    }
}

