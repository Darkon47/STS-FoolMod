package eatyourbeets.actions.special;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.SFX;

public class PlaySFX extends EYBActionWithCallback<PlaySFX>
{
    public final String key;

    private float pitchMin;
    private float pitchMax;

    public PlaySFX(String key)
    {
        this(key, 1, 1);
    }

    public PlaySFX(String key, float pitchMin, float pitchMax)
    {
        super(ActionType.WAIT, 0.1f);

        this.key = key;
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;
        this.isRealtime = true;
    }

    @Override
    protected void FirstUpdate()
    {
        Play();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(this);
        }
    }

    public void Play()
    {
        float seconds = SFX.Play(key, pitchMin, pitchMax);
        if (callbacks.size() > 0)
        {
            duration = seconds;
            startDuration = duration + 0.001f;
        }
        else
        {
            Complete();
        }
    }
}
