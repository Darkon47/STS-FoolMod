package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect_Orb;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_TriggerOrb extends BaseEffect_Orb
{
    public static final String ID = Register(BaseEffect_TriggerOrb.class);

    public BaseEffect_TriggerOrb()
    {
        this( 0, null);
    }

    public BaseEffect_TriggerOrb(SerializedData content)
    {
        super(content);
    }

    public BaseEffect_TriggerOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, PCLCardTarget.None, amount, orb);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Trigger(orb != null ? orb.Tooltip : PGR.PCL.Strings.CardEditor.Orbs, amount, true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.Trigger(PGR.PCL.Strings.CardEditor.Orbs, "X", false);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TriggerOrbPassive(amount).SetFilter(o -> orb == null || o.ID.equals(orb.ID));
    }
}
