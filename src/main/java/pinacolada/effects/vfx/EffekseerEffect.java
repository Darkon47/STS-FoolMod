package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import pinacolada.effects.PCLEffect;
import stseffekseer.STSEffekseerManager;

public class EffekseerEffect extends PCLEffect {
    protected Vector2 position;
    protected Vector3 rotation;
    protected String key;
    protected Integer handle;

    public EffekseerEffect(String key, float x, float y) {
        super(0.5f, false);
        this.key = key;
        this.position = new Vector2(x, y);
    }

    public EffekseerEffect SetRotation(Vector3 rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        handle = STSEffekseerManager.Play(key, position, rotation);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        //STSEffekseerManager.Update(deltaTime);
        if (handle != null && !STSEffekseerManager.Exists(handle)) {
            Complete();
        }
        //super.UpdateInternal(deltaTime);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        STSEffekseerManager.Render(sb);
    }
}
