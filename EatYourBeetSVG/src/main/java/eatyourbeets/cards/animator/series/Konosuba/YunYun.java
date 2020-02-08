package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YunYun extends AnimatorCard implements Spellcaster, OnCostRefreshSubscriber
{
    public static final EYBCardData DATA = Register(YunYun.class).SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    private int costModifier = 0;

    public YunYun()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(4, 0);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        costModifier = 0;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
            GameActions.Bottom.Flash(this);
        }

        costModifier = 0;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        costModifier = 0;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        YunYun copy = (YunYun) super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        OnCostRefresh(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE");

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.VFX(new LightningEffect(m1.drawX, m1.drawY));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int attacks = 0;
            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                if (c != this && c.type == CardType.ATTACK)
                {
                    attacks += 1;
                }
            }

            int currentCost = (costForTurn - costModifier);

            costModifier = attacks;

            if (!this.freeToPlayOnce)
            {
                this.setCostForTurn(currentCost + costModifier);
            }
        }
    }
}