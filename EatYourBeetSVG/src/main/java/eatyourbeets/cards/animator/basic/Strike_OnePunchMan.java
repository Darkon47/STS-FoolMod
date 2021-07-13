package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Strike_OnePunchMan extends Strike
{
    public static final String ID = Register(Strike_OnePunchMan.class).ID;

    public Strike_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6, 0, 1);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.OnePunchMan);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        for (AbstractCard c : p.drawPile.getAttacks().group)
        {
            if (c.tags.contains(CardTags.STRIKE))
            {
                GameActions.Bottom.MoveCard(c, p.drawPile, p.hand);
                return;
            }
        }
    }
}