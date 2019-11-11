package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ExhaustFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Mikaela extends AnimatorCard
{
    public static final String ID = Register(Mikaela.class.getSimpleName(), EYBCardBadge.Special);

    public Mikaela()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6,0, 4);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (p.discardPile.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ExhaustFromPileAction(1, false, p.discardPile, false));
        }

        if (!PlayerStatistics.IsAttacking(m.intent))
        {
            GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}