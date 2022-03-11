package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect_Orb;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_ChannelOrb extends BaseEffect_Orb
{
    public static final String ID = Register(BaseEffect_ChannelOrb.class);

    public BaseEffect_ChannelOrb()
    {
        this(0, null);
    }

    public BaseEffect_ChannelOrb(SerializedData content)
    {
        super(content);
    }

    public BaseEffect_ChannelOrb(int amount, PCLOrbHelper orb)
    {
        super(ID, PCLCardTarget.None, amount, orb);
    }

    @Override
    public String GetText()
    {
        return orb != null ? PGR.PCL.Strings.Actions.Channel(amount, orb.Tooltip, true) : PGR.PCL.Strings.Actions.ChannelRandomOrbs(amount, true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.Channel("X", PGR.PCL.Strings.CardEditor.Orbs, false);
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
