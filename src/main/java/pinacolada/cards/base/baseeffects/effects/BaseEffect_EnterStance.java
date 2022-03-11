package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;

public class BaseEffect_EnterStance extends BaseEffect
{
    public static final String ID = Register(BaseEffect_EnterStance.class);

    protected final PCLStanceHelper stance;

    public BaseEffect_EnterStance() {
        this(null);
    }

    public BaseEffect_EnterStance(PCLStanceHelper stance)
    {
        super(ID, stance != null ? stance.ID : null, PCLCardTarget.Self, 1);
        this.stance = stance;
    }

    @Override
    public String GetText()
    {
        if (stance == null) {
            return PGR.PCL.Strings.Actions.ExitStance(true);
        }
        return PGR.PCL.Strings.Actions.EnterStance("{" + stance.Tooltip.title.replace(stance.Affinity.PowerName, stance.Affinity.GetFormattedPowerSymbol()) + "}", true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (stance == null) {
            PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
        }
        else {
            PCLActions.Bottom.ChangeStance(stance);
        }
    }
}
