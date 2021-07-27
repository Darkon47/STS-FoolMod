package eatyourbeets.actions.special;

import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.SFX;

public class PlaySFX extends EYBAction//WithCallback<AbstractGameEffect> TODO: callback when sound is finished
{
    private String key;
    private float pitch;
    private boolean variable;

    public PlaySFX(String key)
    {
        this(key, 0.0F, false);
    }

    public PlaySFX(String key, float pitch)
    {
        this(key, pitch, false);
    }

    public PlaySFX(String key, float pitch, boolean variable)
    {
        super(ActionType.WAIT);

        this.key = key;
        this.pitch = pitch;
        this.variable = variable;
        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        Play();

        this.isDone = true;
    }

    public String GetKey()
    {
        return key;
    }

    public void Play()
    {
        SFX.Play(key, pitch, variable);
    }
}
