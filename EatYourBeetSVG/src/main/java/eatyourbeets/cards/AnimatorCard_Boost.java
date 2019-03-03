package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import eatyourbeets.Utilities;

public abstract class AnimatorCard_Boost extends AnimatorCard
{
    protected abstract int GetBaseBoost();

    protected AnimatorCard_Boost(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(id, cost, type, rarity, target);
        this.baseSecondaryValue = this.secondaryValue = GetBaseBoost();
    }

    protected boolean ProgressBoost()
    {
        if (this.secondaryValue > 0)
        {
            int newValue = this.secondaryValue - 1;

            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
            {
                AnimatorCard_Boost card = Utilities.SafeCast(c, AnimatorCard_Boost.class);
                if (card != null)
                {
                    if (newValue == 0)
                    {
                        card.baseSecondaryValue = 1;
                        card.secondaryValue = 0;
                        card.isSecondaryValueModified = true;
                    }
                    else
                    {
                        card.baseSecondaryValue = card.secondaryValue = newValue;
                    }
                }
            }

            return true;
        }

        return false;
    }
}
