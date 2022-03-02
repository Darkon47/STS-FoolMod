package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.SFX;
import stseffekseer.STSEffekseerManager;

public class EffekseerEffect extends PCLEffect {
    protected Vector2 position;
    protected Vector3 rotation;
    protected String sfxKey;
    protected String vfxKey;
    protected float pitchMin = 1f;
    protected float pitchMax = 1f;
    protected float volume = 1f;
    protected Integer handle;
    protected Vector3 scale;
    protected boolean hasPlayed;


    public EffekseerEffect(String key, float x, float y) {
        super(0.5f, false);
        this.vfxKey = key;
        this.position = new Vector2(x, y);
    }

    public EffekseerEffect SetRotation(float x, float y, float z) {
        this.rotation = new Vector3(x, y, z);
        return this;
    }

    public EffekseerEffect SetRotation(Vector3 rotation) {
        this.rotation = rotation;
        return this;
    }

    public EffekseerEffect SetScale(float scale) {
        this.scale = new Vector3(scale, scale, scale);
        return this;
    }

    public EffekseerEffect SetScale(Vector3 scale) {
        this.scale = scale;
        return this;
    }

    public EffekseerEffect SetSoundKey(String sfxKey) {
        return SetSoundKey(sfxKey, 1f, 1f, 1f, duration);
    }

    public EffekseerEffect SetSoundKey(String sfxKey, float delay) {
        return SetSoundKey(sfxKey, 1f, 1f, 1f, delay);
    }

    public EffekseerEffect SetSoundKey(String sfxKey, float pitchMin, float pitchMax, float volume, float delay) {
        this.sfxKey = sfxKey;
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;
        this.volume = volume;
        this.duration = delay;
        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        handle = STSEffekseerManager.Play(vfxKey, position, rotation, scale, color);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        this.duration -= deltaTime;
        if (!hasPlayed && this.duration < 0 && sfxKey != null) {
            SFX.Play(sfxKey, pitchMin, pitchMax, volume);
            hasPlayed = true;
        }
        if (handle != null && !STSEffekseerManager.Exists(handle)) {
            Complete();
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        STSEffekseerManager.Render(sb);
    }
}
