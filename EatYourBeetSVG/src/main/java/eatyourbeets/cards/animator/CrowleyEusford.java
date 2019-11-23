package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnCostRefreshSubscriber;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard implements OnCostRefreshSubscriber
{
    public static final String ID = Register(CrowleyEusford.class.getSimpleName(), EYBCardBadge.Special);

    private int costModifier = 0;

    public CrowleyEusford()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(5, 0, 3, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        resetAttributes();
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
        CrowleyEusford copy = (CrowleyEusford)super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        OnCostRefresh(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, secondaryValue, false), secondaryValue);

        for (int i = 1; i < magicNumber; i++)
        {
            GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        GameActionsHelper.DamageRandomEnemyWhichActuallyWorks(p, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int currentCost = (costForTurn + costModifier);

            costModifier = PlayerStatistics.getCardsExhaustedThisTurn();

            if (!this.freeToPlayOnce)
            {
                setCostForTurn(currentCost - costModifier);
            }
        }
    }
}