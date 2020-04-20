package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class AgilityStance extends EYBStance
{
    public static final String STANCE_ID = CreateFullID(AgilityStance.class);

    public AgilityStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Bottom.GainAgility(1, true);

        ApplyPowerSilently(PowerHelper.Focus    , -1);
        ApplyPowerSilently(PowerHelper.Dexterity,  2);
        ApplyPowerSilently(PowerHelper.Strength , -1);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        ApplyPowerSilently(PowerHelper.Focus    ,  1);
        ApplyPowerSilently(PowerHelper.Dexterity, -2);
        ApplyPowerSilently(PowerHelper.Strength ,  1);
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleHorizontal(GetParticleColor()));
        GameEffects.Queue.Add(new StanceParticleVertical(GetAuraColor()));
    }

    @Override
    protected void QueueAura()
    {
        //GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }
}