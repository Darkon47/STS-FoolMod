package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE).SetMaxCopies(1).SetSeriesFromClassPackage().SetMultiformData(2);
    private static final int POWER_ENERGY_COST = 7;

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 4, 6);
        SetUpgrade(0, 0, 0);
        SetAffinity_Orange(2, 0, 0);
        SetDelayed(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetDelayed(form == 1);
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST, POWER_ENERGY_COST);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Earth());
        GameActions.Bottom.ApplyPower(new ZhongliPower(p, this, this.magicNumber, this.secondaryValue));
    }

    public static class ZhongliPower extends AnimatorClickablePower implements OnStartOfTurnPostDrawSubscriber
    {
        private static final CardEffectChoice choices = new CardEffectChoice();

        private final Zhongli zhongli;
        public int gainAmount = 0;
        public int secondaryValue;

        public ZhongliPower(AbstractPlayer owner, Zhongli zhongli, int amount, int secondaryValue)
        {
            super(owner, Zhongli.DATA, PowerTriggerConditionType.Affinity, POWER_ENERGY_COST, null, null, Affinity.Orange);

            this.amount = amount;
            this.zhongli = zhongli;
            this.secondaryValue = secondaryValue;
            this.triggerCondition.SetOneUsePerPower(true);

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.ChannelOrb(new Earth());

            if (choices.TryInitialize(this.zhongli))
            {
                choices.AddEffect( JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[1],amount), (c,p,mo) -> {GameActions.Bottom.GainResistance(amount, true);});
                choices.AddEffect( JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],amount), (c,p,mo) -> {
                    gainAmount += amount;
                    CombatStats.onStartOfTurnPostDraw.Subscribe(this);
                });
            }
            choices.Select(1, m);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_ENERGY_COST, amount, secondaryValue);
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            Earth earthOrb = JUtils.SafeCast(orb, Earth.class);

            if (earthOrb != null) {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            GameActions.Bottom.GainResistance(gainAmount, true);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            gainAmount = 0;
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryValue, Color.WHITE, c.a);
        }
    }
}