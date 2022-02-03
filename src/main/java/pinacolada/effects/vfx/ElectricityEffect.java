package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import pinacolada.utilities.PCLGameEffects;

public class ElectricityEffect extends EYBEffect
{
    protected int particles = 60;
    protected float spread = 70;
    protected float jitter = 48;
    protected float x;
    protected float y;

    public ElectricityEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.color = Color.GOLDENROD.cpy();
    }

    public ElectricityEffect SetParticleCount(int particles)
    {
        this.particles = particles;

        return this;
    }

    public ElectricityEffect SetSpread(float spread)
    {
        this.spread = spread;

        return this;
    }

    public ElectricityEffect SetJitter(float jitter)
    {
        this.jitter = jitter;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        PCLGameEffects.Queue.Add(new SparkImpactEffect(this.x, this.y));
        for (int i = 0; i < particles; i++)
        {
            PCLGameEffects.Queue.Add(new ElectricityParticleEffect(x + Random(-spread, spread) * Settings.scale,
                    y + Random(-spread, spread) * Settings.scale,
                    jitter * Settings.scale,
                    color)
            .SetDuration(MathUtils.random(0.2F, 1.0F), true));
        }

        Complete();
    }
}
