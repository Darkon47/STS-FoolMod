package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.ui.TextureCache;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLJUtils;
import pinacolada.utilities.PCLRenderHelpers;

public class StarParticleEffect extends PCLEffect
{
    protected static final TextureCache[] images = { VFX.IMAGES.Sparkle1, VFX.IMAGES.Sparkle2, VFX.IMAGES.Sparkle3, VFX.IMAGES.Sparkle4 };

    protected float x;
    protected float y;
    protected float horizontalSpeed;
    protected float verticalSpeed;
    protected float rotationSpeed;
    protected float alpha;
    protected float halfDuration;
    protected boolean flipX;
    protected boolean translucent;
    protected Texture image;

    public StarParticleEffect(float x, float y, float horizontalSpeed, float verticalSpeed, float scale, Color mainColor)
    {
        super(MathUtils.random(0.4F, 0.8F));

        final float offsetX = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }

        this.x = x + offsetX;
        this.y = y + offsetY;
        this.horizontalSpeed = horizontalSpeed * Settings.scale;
        this.verticalSpeed = verticalSpeed * Settings.scale;
        this.image = PCLJUtils.Random(images).Texture();
        this.color = mainColor.cpy();
        this.color.a = this.alpha = Random(0.5F, 1.0F);
        this.scale = scale;
        this.flipX = RandomBoolean();
        this.rotation = Random(-10f, 10f);
        this.rotationSpeed = Random(-12f, 12f);
        this.translucent = RandomBoolean();
        this.halfDuration = startingDuration * 0.5f;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        this.rotation += this.rotationSpeed;
        if (this.duration < halfDuration)
        {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
        }
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image, x, y, flipX, false, this.translucent ? pinacolada.utilities.PCLRenderHelpers.BlendingMode.Glowing : PCLRenderHelpers.BlendingMode.Normal);
    }
}
