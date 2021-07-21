package eatyourbeets.resources.animator.misc;

import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

public class ConfigOption_String extends ConfigOption<String>
{
    public String DefaultValue;

    public ConfigOption_String(SpireConfig config, String key, String defaultValue)
    {
        super(config, key);

        DefaultValue = defaultValue;
    }

    @Override
    public String Get()
    {
        if (Value == null)
        {
            if (Config.has(Key))
            {
                Value = Config.getString(Key);
            }
            else
            {
                Value = DefaultValue;
            }
        }

        return Value;
    }

    @Override
    public String Set(String value, boolean save)
    {
        Value = value;
        Config.setString(Key, value);

        if (save)
        {
            Save();
        }

        return Value;
    }

    @Override
    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {

    }
}
