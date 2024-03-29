package pinacolada.powers.fool;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.effects.SFX;
import pinacolada.interfaces.listeners.OnTryReducePowerListener;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.common.RippledPower;

public class StonedPower extends PCLPower implements OnTryReducePowerListener
{
    public static final String POWER_ID = CreateFullID(StonedPower.class);

    public StonedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.RELIC_DROP_ROCKY, 2.1f, 2.3f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        ReducePower(1);
    }

    @Override
    public boolean TryReducePower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
    	boolean returnValue = true;
    	try {
    		if(target != owner) {
    			returnValue = false;
    		}
    		if(!BurningPower.POWER_ID.equals(power.ID)) {
    			returnValue = false;
    		}
    		if(!FreezingPower.POWER_ID.equals(power.ID)) {
    			returnValue = false;
    		}
    		if(!ElectrifiedPower.POWER_ID.equals(power.ID)) {
    			returnValue = false;
    		}
    		if(!RippledPower.POWER_ID.equals(power.ID)) {
    			returnValue = false;
    		}
    	}
    	catch(NullPointerException e) {
    		
    	}
        return returnValue;
    }
}