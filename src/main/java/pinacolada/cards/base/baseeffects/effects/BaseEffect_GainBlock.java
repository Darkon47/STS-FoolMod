package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.BlockAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_GainBlock extends BaseEffect
{
    public static final String ID = Register(BaseEffect_GainBlock.class);

    public BaseEffect_GainBlock(int amount)
    {
        super(ID, null, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.GainAmount(amount, PGR.Tooltips.Block, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(amount);
    }

    @Override
    public AbstractAttribute GetBlockInfo() {
        return BlockAttribute.Instance
                .SetIcon(PGR.PCL.Images.Icons.Block.Texture())
                .SetLargeIcon(PGR.PCL.Images.Icons.Block_L.Texture())
                .SetText(new ColoredString(amount, Settings.CREAM_COLOR));
    }
}
