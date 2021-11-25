package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnSynergyBonusSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class)
            .SetPower(2, CardRarity.RARE)
            .SetMultiformData(2)
            .SetSeriesFromClassPackage();
    public static final int COST = 7;

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 0, 2, COST);

        SetAffinity_Red(2);
        SetAffinity_Green(2);

        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetDelayed(false);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainArtifact(1);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, magicNumber));
    }

    public static class BiyorigoPower extends AnimatorClickablePower implements OnSynergyBonusSubscriber
    {
        public static final int MAX_METALLICIZE = 4;

        public BiyorigoPower(AbstractCreature owner, int amount)
        {
            super(owner, Biyorigo.DATA, PowerTriggerConditionType.Affinity, COST, null, null, Affinity.Green);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergyBonus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergyBonus.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, COST, amount, MAX_METALLICIZE);
        }

        @Override
        public void OnSynergyBonus(AbstractCard card, Affinity affinity)
        {
            if (Affinity.Red.equals(affinity))
            {
                GameActions.Bottom.GainMetallicize(Math.min(MAX_METALLICIZE, amount));
                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            GameActions.Bottom.GainThorns(2);
        }
    }
}