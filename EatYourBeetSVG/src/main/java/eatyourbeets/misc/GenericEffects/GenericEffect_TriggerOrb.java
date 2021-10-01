package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_TriggerOrb extends GenericEffect
{
    private AbstractOrb orb;
    private FuncT0<AbstractOrb> orbConstructor;

    public GenericEffect_TriggerOrb(AbstractOrb orb)
    {
        this.orbConstructor = null;
        this.orb = orb;
        this.amount = 1;
        this.tooltip = GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    public GenericEffect_TriggerOrb(int amount)
    {
        this.orbConstructor = null;
        this.orb = null;
        this.amount = amount;
        this.tooltip = GR.Tooltips.RandomOrb;
        this.id = tooltip.id;
    }

    public GenericEffect_TriggerOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        this.orbConstructor = orbConstructor;
        this.orb = orbConstructor.Invoke();
        this.amount = amount;
        this.tooltip =  GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.Trigger("[" + tooltip.id + "]", amount, true);
    }

    public EYBCardTooltip GetOrbTooltip(AbstractOrb orb)
    {
        return CardTooltips.FindByID(orb.ID.replace(AnimatorResources.ID + ":", ""));
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> o.ID.equals(orb.ID));
    }
}
