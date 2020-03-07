package eatyourbeets.utilities;

import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

public class GenericCallback<T>
{
    protected Object state;
    protected ActionT0 actionT0;
    protected ActionT1<T> actionT1;
    protected ActionT2<Object, T> actionT2;

    public static <T> GenericCallback<T> FromT0(ActionT0 onCompletion)
    {
        return new GenericCallback<T>(onCompletion);
    }

    public static <T> GenericCallback<T> FromT1(ActionT1<T> onCompletion)
    {
        return new GenericCallback<T>(onCompletion);
    }

    public static <T> GenericCallback<T> FromT2(ActionT2<Object, T> onCompletion, Object state)
    {
        return new GenericCallback<T>(onCompletion, state);
    }

    private GenericCallback(ActionT2<Object, T> onCompletion, Object state)
    {
        this.state = state;
        this.actionT2 = onCompletion;
    }

    private GenericCallback(ActionT1<T> onCompletion)
    {
        this.actionT1 = onCompletion;
    }

    private GenericCallback(ActionT0 onCompletion)
    {
        this.actionT0 = onCompletion;
    }

    public void Complete(T result)
    {
        if (actionT2 != null)
        {
            actionT2.Invoke(state, result);
        }

        if (actionT1 != null)
        {
            actionT1.Invoke(result);
        }

        if (actionT0 != null)
        {
            actionT0.Invoke();
        }
    }
}
