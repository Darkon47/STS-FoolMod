package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_ChannelOrb extends BaseEffect
{
    public static final String ID = Register(BaseEffect_ChannelOrb.class);

    protected final PCLOrbHelper orb;

    public BaseEffect_ChannelOrb(String[] content)
    {
        super(content);
        this.orb = PCLOrbHelper.Get(entityID);
    }

    public BaseEffect_ChannelOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, orb != null ? orb.ID : null, PCLCardTarget.None, amount);
        this.orb = orb;
    }

    @Override
    public String GetText()
    {
        return orb != null ? PGR.PCL.Strings.Actions.Channel(amount, orb.Tooltip, true) : PGR.PCL.Strings.Actions.ChannelRandomOrbs(amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (orb != null) {
            PCLActions.Bottom.ChannelOrbs(orb, amount);
        }
        else {
            PCLActions.Bottom.ChannelRandomOrbs(amount);
        }
    }
}
