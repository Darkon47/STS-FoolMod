package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class DarknessEffect extends EYBEffect
{
    private static final TextureCache[] images = {VFX.IMAGES.Dark1, VFX.IMAGES.Dark2, VFX.IMAGES.Dark3, VFX.IMAGES.Dark4, VFX.IMAGES.Dark5};

    protected float x;
    protected float y;

    public DarknessEffect(float startX, float startY)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        for (int i = images.length - 1; i >= 0; i--) {
            PCLGameEffects.Queue.Add(new FadingParticleEffect(images[i].Texture(), x, y)
                    .SetColor(new Color(0.47f, 0.35f, 0.6f, 0.4f))
                    .SetBlendingMode(i <= 2 ? PCLRenderHelpers.BlendingMode.Glowing : PCLRenderHelpers.BlendingMode.Normal)
                    .SetScale(scale * MathUtils.random(0.2f, i < 2 ? 0.7f : 0.4f))
                    .SetTargetScale(scale * MathUtils.random(1.7f, i < 2 ? 4.3f : 2.6f), 2.2f)
                    .SetRotation(MathUtils.random(-800f, 800f))
                    .SetTargetRotation(76000f,MathUtils.random(400, 600f) * i % 2 == 0 ? -1 : 1)
                    .SetDuration(MathUtils.random(0.8F, 1.0F), true));
        }

        super.UpdateInternal(deltaTime);
    }
}
