package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class HinaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HinaPower.class.getSimpleName());
    public static final int CARD_DRAW_AMOUNT = 2;
    private int baseAmount;
    public HinaPower(AbstractCreature owner, int amount)
    {
        super(owner, HinaKagiyama.DATA);

        this.amount = amount;
        this.baseAmount = amount;
        FormatDescription(0, amount, CARD_DRAW_AMOUNT);
        updateDescription();
    }
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.baseAmount += stackAmount;
        updateDescription();
    }
    @Override
    public void atStartOfTurn()
    {
        this.amount = baseAmount;
        updateDescription();
    }
    @Override
    public void onCardDraw(AbstractCard c)
    {
        if(c.type == AbstractCard.CardType.CURSE && this.amount > 0)
            GameActions.Bottom.Draw(CARD_DRAW_AMOUNT);
            this.amount--;
            this.flash();
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer){
        if (AbstractDungeon.player.gameHandSize > 2){
            if (JavaUtilities.Count(AbstractDungeon.player.hand.group, GameUtilities::IsCurseOrStatus) > 2 && EffectHistory.TryActivateLimited(POWER_ID)){
                GameActions.Bottom.ChannelOrb(new Plasma(), false);
            }
        }
    }
}

