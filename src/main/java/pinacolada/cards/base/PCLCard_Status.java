package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;

// TODO Remove
public abstract class PCLCard_Status extends PCLCard
{
    protected PCLCard_Status(PCLCardData data)
    {
        super(data);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        this.cardText.OverrideDescription(form > 0 && cardData.Strings.EXTENDED_DESCRIPTION != null && cardData.Strings.EXTENDED_DESCRIPTION.length >= form ? cardData.Strings.EXTENDED_DESCRIPTION[form - 1] : null, true);
        this.portraitImg.color = form != 0 ? new Color(0.5f, 0.5f, 0.5f, 1f) : Color.WHITE.cpy();
        return super.SetForm(form, timesUpgraded);
    }
}