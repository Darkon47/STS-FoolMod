package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_TriggerOrb extends BaseEffect
{
    public static final String ID = Register(BaseEffect_TriggerOrb.class);

    protected final PCLOrbHelper orb;

    public BaseEffect_TriggerOrb()
    {
        this( 0, null);
    }

    public BaseEffect_TriggerOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, orb != null ? orb.ID : null, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Trigger(orb != null ? orb.Tooltip : GR.Tooltips.RandomOrb, amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> orb == null || o.ID.equals(orb.ID));
    }
}
