package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class HighElfArcher extends AnimatorCard_Boost
{
    public static final String ID = Register(HighElfArcher.class.getSimpleName(), EYBCardBadge.Synergy);

    public HighElfArcher()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(3, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (ProgressBoost())
        {
            GameActionsHelper.GainAgility(1);
        }

        if (HasActiveSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.GainAgility(1);
            GameActionsHelper.DrawCard(p, 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeBoost(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 2 : 1;
    }
}