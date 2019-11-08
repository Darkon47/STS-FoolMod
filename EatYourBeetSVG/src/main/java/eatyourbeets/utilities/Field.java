package eatyourbeets.utilities;

public class Field<T>
{
    private final java.lang.reflect.Field field;

    public void Set(Object instance, T value)
    {
        try
        {
            field.set(instance, value);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public T Get(Object instance)
    {
        try
        {
            return (T)field.get(instance);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Field(java.lang.reflect.Field field)
    {
        this.field = field;
    }
}