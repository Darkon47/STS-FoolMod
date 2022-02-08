package pinacolada.blights;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.localization.BlightStrings;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class PCLBlight extends AbstractBlight
{
    protected final int initialAmount;
    public final BlightStrings strings;
    public ArrayList<PCLCardTooltip> tips;
    public PCLCardTooltip mainTooltip;

    public static String CreateFullID(Class<? extends PCLBlight> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public PCLBlight(String id)
    {
        this(id, GR.GetBlightStrings(id), -1);
    }

    public PCLBlight(String id, int amount)
    {
        this(id, GR.GetBlightStrings(id), amount);
    }

    public PCLBlight(String id, BlightStrings strings, int amount)
    {
        super(id, strings.NAME, PCLJUtils.Format(strings.DESCRIPTION[0], amount), "durian.png", true);

        this.img = GR.GetTexture(GR.GetBlightImage(id));
        this.outlineImg = GR.GetTexture(GR.GetBlightOutlineImage(id));
        this.initialAmount = amount;
        this.counter = amount;
        this.strings = strings;
        if (tips == null) {
            initializeTips();
        }
    }

    @Override
    protected void initializeTips()
    {
        if (tips == null)
        {
            tips = new ArrayList<>();
        }
        else
        {
            tips.clear();
        }

        mainTooltip = new PCLCardTooltip(name, description);
        tips.add(mainTooltip);
        PCLGameUtilities.ScanForTips(description, tips);
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        PCLCardTooltip.QueueTooltips(this);
    }

    @Override
    public void updateDescription() {
        super.updateDescription();
        if (tips == null) {
            initializeTips();
        }
        if (tips.size() > 0) {
            tips.get(0).description = description;
        }
    }


    protected String FormatDescription(int index, Object... args)
    {
        return PCLJUtils.Format(strings.DESCRIPTION[index], args);
    }

    public PCLBlight makeCopy() {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            PCLJUtils.LogError(this, e.getMessage());
            return null;
        }
    }
}
