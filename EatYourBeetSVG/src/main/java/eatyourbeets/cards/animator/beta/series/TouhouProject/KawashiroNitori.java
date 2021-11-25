package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnTagChangedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class KawashiroNitori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KawashiroNitori.class)
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
        GameActions.Bottom.StackPower(new KawashiroNitoriPower(p, magicNumber));
    }

    public static class KawashiroNitoriPower extends AnimatorClickablePower implements OnTagChangedSubscriber
    {

        public KawashiroNitoriPower(AbstractCreature owner, int amount)
        {
            super(owner, KawashiroNitori.DATA, PowerTriggerConditionType.Affinity, 5);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onTagChanged.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onTagChanged.Unsubscribe(this);
        }


        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.SelectFromHand(name, 1, false)
                    .SetOptions(true, true, true)
                    .SetMessage(GR.Common.Strings.GridSelection.Give(amount, GR.Tooltips.Retain.title))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards) {
                            GameActions.Bottom.ModifyTag(c, HASTE, true);
                        }
                    });
        }

        @Override
        public void OnTagChanged(AbstractCard card, CardTags tag, boolean value) {
            for (Affinity af : Affinity.Extended()) {
                GameActions.Bottom.AddAffinity(af, amount);
            }
            flash();
        }
    }
}

