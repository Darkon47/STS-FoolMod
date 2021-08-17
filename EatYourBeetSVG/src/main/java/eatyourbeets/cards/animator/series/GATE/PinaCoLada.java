package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PinaCoLada extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PinaCoLada.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public PinaCoLada()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);

        SetAffinity_Light(1, 1, 0);
        SetAffinity_Orange(2, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new PinaCoLadaPower(p, 1));
    }

    public class PinaCoLadaPower extends AnimatorClickablePower
    {
        private static final int DISCARD_AMOUNT = 2;

        public PinaCoLadaPower(AbstractCreature owner, int amount)
        {
            super(owner, PinaCoLada.DATA, PowerTriggerConditionType.Discard, DISCARD_AMOUNT);

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.ChannelOrb(new Earth());
            GameActions.Bottom.StackPower(new DrawCardNextTurnPower(player, 1));
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, DISCARD_AMOUNT);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if ((card.costForTurn == 0 || card.freeToPlayOnce) && amount > 0 && GameUtilities.CanPlayTwice(card))
            {
                //GameActions.Top.PlayCopy(card, (AbstractMonster)((action.target == null) ? null : action.target));
                GameActions.Top.Callback(() -> card.use(player, (AbstractMonster)((action.target == null) ? null : action.target)));
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }
    }
}