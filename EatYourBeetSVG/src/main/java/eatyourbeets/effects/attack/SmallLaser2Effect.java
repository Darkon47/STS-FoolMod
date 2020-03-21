package eatyourbeets.effects.attack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

@SuppressWarnings("FieldCanBeLocal")
public class SmallLaser2Effect extends AbstractGameEffect
{
    private final float sX;
    private final float sY;
    private final float dX;
    private final float dY;
    private final float dst;
    private static final float DUR = 0.5f;
    private static AtlasRegion img;

    public SmallLaser2Effect(float sX, float sY, float dX, float dY, Color color)
    {
        if (img == null)
        {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }

        this.sX = sX;
        this.sY = sY;
        this.dX = dX;
        this.dY = dY;
        this.dst = Vector2.dst(this.sX, this.sY, this.dX, this.dY) / Settings.scale;
        this.color = color.cpy();
        this.duration = 0.5f;
        this.startingDuration = 0.5f;
        this.rotation = MathUtils.atan2(dX - sX, dY - sY);
        this.rotation *= 57.295776f;
        this.rotation = -this.rotation + 90f;
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2f)
        {
            this.color.a = Interpolation.pow2In.apply(1f, 0f, (this.duration - 0.25f) * 4f);
        }
        else
        {
            this.color.a = Interpolation.bounceIn.apply(0f, 1f, this.duration * 4f);
        }

        if (this.duration < 0f)
        {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img, this.sX, this.sY - (float) img.packedHeight / 2f + 10f * Settings.scale, 0f, (float) img.packedHeight / 2f, this.dst, 50f, this.scale + MathUtils.random(-0.01f, 0.01f), this.scale, this.rotation);
        sb.setColor(new Color(0.3f, 0.3f, 1f, this.color.a));
        sb.draw(img, this.sX, this.sY - (float) img.packedHeight / 2f, 0f, (float) img.packedHeight / 2f, this.dst, MathUtils.random(50f, 90f), this.scale + MathUtils.random(-0.02f, 0.02f), this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {

    }
}
