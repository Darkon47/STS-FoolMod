package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.Mathf;

public class ThrowProjectileEffect extends EYBEffectWithCallback<Hitbox>
{
    protected AdvancedTexture projectile;
    protected Hitbox target;
    protected float offset_x;
    protected float offset_y;
    protected Float speed;

    public ThrowProjectileEffect(AdvancedTexture projectile, Hitbox target)
    {
        super(Settings.ACTION_DUR_FAST, true);

        this.target = target;
        this.projectile = projectile;
        this.offset_x = this.offset_y = 0;
    }

    public ThrowProjectileEffect SetSpread(float spreadX, float spreadY)
    {
        this.offset_x = spreadX;
        this.offset_y = spreadY;

        return this;
    }

    public ThrowProjectileEffect SetScale(float scale)
    {
        this.projectile.SetScale(scale);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        final float target_x = target.cX + (Random(-offset_x, offset_x) * target.width);
        final float target_y = target.cY + (Random(-offset_y, offset_y) * target.height);
        projectile.SetTargetPosition(target_x, target_y);

        final float speed_x = Mathf.Abs(projectile.target_pos.x - projectile.current_pos.x) / duration;
        final float speed_y = Mathf.Abs(projectile.target_pos.y - projectile.current_pos.y) / duration;
        projectile.SetSpeed(speed_x, speed_y, null);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        projectile.Update(deltaTime);

        if (TickDuration(deltaTime))
        {
            Complete(target);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (projectile != null)
        {
            projectile.Render(sb);
        }
    }
}
