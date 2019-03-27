package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class BattleDrones extends AnimatorRelic
{
    public static final String ID = CreateFullID(BattleDrones.class.getSimpleName());

    private static final int DAMAGE_AMOUNT = 3;
    private static final int BLOCK_AMOUNT = 1;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DAMAGE_AMOUNT + DESCRIPTIONS[1] + BLOCK_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new GainBlockAction(p, p, BLOCK_AMOUNT, true));
            GameActionsHelper.DamageRandomEnemy(p, DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);

            this.flash();
        }
    }
}