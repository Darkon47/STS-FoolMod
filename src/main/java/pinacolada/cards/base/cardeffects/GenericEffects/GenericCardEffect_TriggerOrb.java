package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class GenericCardEffect_TriggerOrb extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_TriggerOrb.class);

    protected final PCLOrbHelper orb;

    public GenericCardEffect_TriggerOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, orb.ID, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Trigger(orb.Tooltip, amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> o.ID.equals(orb.ID));
    }
}
