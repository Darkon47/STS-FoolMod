package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class GenericEffect_PayAffinity extends GenericEffect
{
    protected final Affinity affinity;

    public GenericEffect_PayAffinity(Affinity affinity, int amount)
    {
        this.affinity = affinity;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.PayCost(amount, affinity.GetTooltip(), true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameUtilities.TrySpendAffinity(affinity,amount,true);
    }
}
