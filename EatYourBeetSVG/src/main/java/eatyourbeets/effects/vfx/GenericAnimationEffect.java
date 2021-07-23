package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.effects.AnimatedProjectile;
import eatyourbeets.effects.EYBEffect;

public abstract class GenericAnimationEffect extends EYBEffect
{

    protected AnimatedProjectile projectile;
    protected float x;
    protected float y;
    protected float vR;
    protected int endFrame;

    public GenericAnimationEffect(String imgUrl, float x, float y, float size, float animTimer, int totalFrames) {
        this.x = x;
        this.y = y;

        this.projectile = new AnimatedProjectile(imgUrl, size, animTimer, totalFrames);
        this.projectile.SetPosition(x,y).SetTargetPosition(x,y);
        this.endFrame = this.projectile.totalFrames;
    }

    public GenericAnimationEffect(String imgUrl, float x, float y, float size, float animTimer) {
        this(imgUrl,x,y,size,0.03F, Integer.MAX_VALUE);
    }

    public GenericAnimationEffect(String imgUrl, float x, float y, float size) {
        this(imgUrl,x,y,size,0.03F, Integer.MAX_VALUE);
    }

    public GenericAnimationEffect AdvanceToFrame(int frame){
        this.projectile.frame = frame;
        return this;
    }

    public GenericAnimationEffect SetColor(Color color){
        this.projectile.SetColor(color);
        return this;
    }

    public GenericAnimationEffect SetImageParameters(float scale, float vR, float rotation)
    {
        this.scale = scale;
        this.vR = vR;
        this.rotation = rotation;

        return this;
    }

    public GenericAnimationEffect SetMode(AnimatedProjectile.AnimationMode mode, int duration){
        this.projectile.mode = mode;
        this.endFrame = duration;
        return this;
    }

    public GenericAnimationEffect SetTargetPosition(float x, float y){
        this.projectile.SetTargetPosition(x,y);
        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        this.projectile.Update(deltaTime);
        if (this.projectile.frame >= endFrame) {
            Complete();
        }

        if (this.projectile.mode == AnimatedProjectile.AnimationMode.Fade && this.projectile.frame >= this.projectile.totalFrames) {
            this.projectile.color.a = Interpolation.fade.apply(0f, 1f, (endFrame - this.projectile.frame) / (float)(endFrame - this.projectile.totalFrames));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (projectile != null)
        {
            projectile.Render(sb);
        }
    }

    public void dispose()
    {
    }
}
