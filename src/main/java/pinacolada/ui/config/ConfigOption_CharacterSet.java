package pinacolada.ui.config;

import basemod.ModPanel;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.ui.config.ConfigOption;
import pinacolada.utilities.PCLJUtils;

import java.util.HashSet;
import java.util.Set;

public class ConfigOption_CharacterSet extends ConfigOption<Set<AbstractPlayer.PlayerClass>>
{
    public Set<AbstractPlayer.PlayerClass> DefaultValue;

    public ConfigOption_CharacterSet(String key, Set<AbstractPlayer.PlayerClass> defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public Set<AbstractPlayer.PlayerClass> Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public Set<AbstractPlayer.PlayerClass> Get(Set<AbstractPlayer.PlayerClass> defaultValue)
    {
        if (Value == null)
        {
            if (defaultValue == null)
            {
                defaultValue = new HashSet<AbstractPlayer.PlayerClass>();
            }

            Value = defaultValue;

            if (Config.has(Key))
            {
                final String[] data = PCLJUtils.SplitString("|", Config.getString(Key));
                for (String s : data) {
                    Value.add(AbstractPlayer.PlayerClass.valueOf(s));
                }
            }
        }

        return Value;
    }

    @Override
    public Set<AbstractPlayer.PlayerClass> Set(Set<AbstractPlayer.PlayerClass> value, boolean save)
    {
        Value = value;
        Config.setString(Key, PCLJUtils.JoinStrings("|", PCLJUtils.Map(value, Enum::name)));

        if (save)
        {
            Save();
        }

        return Value;
    }

    @Override
    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {
        throw new RuntimeException("Not implemented");
    }
}
