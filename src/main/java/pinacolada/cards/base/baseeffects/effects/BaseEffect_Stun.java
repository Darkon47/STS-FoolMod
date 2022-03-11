package pinacolada.cards.base.baseeffects.effects;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_Stun extends BaseEffect
{
    public static final String ID = Register(BaseEffect_Stun.class);

    public BaseEffect_Stun()
    {
        this( 0);
    }

    public BaseEffect_Stun(SerializedData content)
    {
        super(content);
    }

    public BaseEffect_Stun(int amount)
    {
        super(ID, null, PCLCardTarget.Normal, amount);
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.Stun(GetTargetString(), true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.Stun("X", true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyPower(p, new StunMonsterPower(m, amount));
    }
}
