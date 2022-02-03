package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class ElectricityParticleEffect extends EYBEffect
{
    private static final TextureCache[] images = {
            VFX.IMAGES.Electric1,
            VFX.IMAGES.Electric2,
            VFX.IMAGES.Electric3,
            VFX.IMAGES.Electric4,
            VFX.IMAGES.Electric5,
            VFX.IMAGES.Electric6,
            VFX.IMAGES.Electric7};
    private static final ArrayList<Texture> imageTextures = new ArrayList<>();

    protected static final int SIZE = 96;

    protected float animFrequency = 0.005f;
    protected float animTimer;
    protected int imgIndex;
    protected Texture img;
    protected float x;
    protected float y;
    protected float baseX;
    protected float baseY;
    protected float jitter;
    protected float vR;
    protected boolean flip;

    public ElectricityParticleEffect(float x, float y, float jitter, Color color)
    {
        super(Random(0.5f, 1f));

        this.img = GetTexture(Random(0, images.length - 1));
        this.x = this.baseX = x - (float) (SIZE / 2);
        this.y = this.baseY = y - (float) (SIZE / 2);
        this.jitter = jitter;
        this.rotation = Random(-10f, 10f);
        this.scale = Random(0.2f, 1.5f) * Settings.scale;
        this.vR = Random(-700f, 700f);
        this.flip = RandomBoolean(0.5f);

        SetColor(color, 0.35f);
    }

    public ElectricityParticleEffect SetColor(Color color, float variance)
    {
        this.color = color.cpy();
        this.color.a = 0;

        if (variance > 0)
        {
            this.color.r = Math.max(0, color.r - Random(0, variance));
            this.color.g = Math.max(0, color.g - Random(0, variance));
            this.color.b = Math.max(0, color.b - Random(0, variance));
        }

        return this;
    }

    public ElectricityParticleEffect SetScale(float scale)
    {
        this.scale = scale;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (scale > 0.3f * Settings.scale)
        {
            scale -= deltaTime * 2f;
        }

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0f, 1f, (1f - duration) * 10f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0f, 1f, duration);
        }

        animTimer -= deltaTime;
        if (animTimer < 0) {
            animTimer = animFrequency;
            this.img = GetTexture(imgIndex);
            x = baseX + Random(-jitter, jitter);
            y = baseY + Random(-jitter, jitter);
            rotation += vR * deltaTime;
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(this.color);
        sb.draw(this.img, x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, flip, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected Texture GetTexture(int currentIndex) {
        if (imageTextures.size() == 0) {
            imageTextures.addAll(PCLJUtils.Map(images, TextureCache::Texture));
        }
        imgIndex = (currentIndex + Random(1, images.length - 1)) % images.length;
        return imageTextures.get(imgIndex);
    }
}
