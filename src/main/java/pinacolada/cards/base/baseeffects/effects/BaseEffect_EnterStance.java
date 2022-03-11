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

    protected PCLStanceHelper stance;

    public BaseEffect_EnterStance() {
        this((PCLStanceHelper) null);
    }

    public BaseEffect_EnterStance(SerializedData content)
    {
        super(content);
        this.stance = PCLStanceHelper.Get(entityID);
    }

    public BaseEffect_EnterStance(PCLStanceHelper stance)
    {
        super(ID, stance != null ? stance.ID : null, PCLCardTarget.Self, 1);
        this.stance = stance;
    }

    public BaseEffect_EnterStance Set(PCLStanceHelper stance) {
        this.stance = stance;
        this.entityID = stance != null ? stance.ID : null;
        return this;
    }

    public PCLStanceHelper GetStance() {
        return stance;
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
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.EnterStance("X", false);
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
