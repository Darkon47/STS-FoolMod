package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.interfaces.markers.TooltipObject;
import pinacolada.resources.PGR;

import java.util.HashMap;
import java.util.Map;

public class PCLCardTagHelper implements TooltipObject
{
    public static final Map<AbstractCard.CardTags, PCLCardTagHelper> ALL = new HashMap<>();

    public static final PCLCardTagHelper Afterlife = new PCLCardTagHelper(PGR.Enums.CardTags.AFTERLIFE, PGR.Tooltips.Afterlife, true, PCLCardTagHelper.Purge);
    public static final PCLCardTagHelper Autoplay = new PCLCardTagHelper(PGR.Enums.CardTags.AUTOPLAY, PGR.Tooltips.Autoplay, false);
    public static final PCLCardTagHelper Delayed = new PCLCardTagHelper(PGR.Enums.CardTags.DELAYED, PGR.Tooltips.Delayed, false);
    public static final PCLCardTagHelper Ethereal = new PCLCardTagHelper(PGR.Enums.CardTags.PCL_ETHEREAL, PGR.Tooltips.Ethereal, false);
    public static final PCLCardTagHelper Exhaust = new PCLCardTagHelper(PGR.Enums.CardTags.PCL_EXHAUST, PGR.Tooltips.Exhaust, false, PCLCardTagHelper.Purge);
    public static final PCLCardTagHelper Fragile = new PCLCardTagHelper(PGR.Enums.CardTags.FRAGILE, PGR.Tooltips.Fragile, false);
    public static final PCLCardTagHelper Haste = new PCLCardTagHelper(PGR.Enums.CardTags.HASTE, PGR.Tooltips.Haste, true, PCLCardTagHelper.HasteInfinite);
    public static final PCLCardTagHelper HasteInfinite = new PCLCardTagHelper(PGR.Enums.CardTags.HASTE_INFINITE, PGR.Tooltips.HasteInfinite, true);
    public static final PCLCardTagHelper Innate = new PCLCardTagHelper(PGR.Enums.CardTags.PCL_INNATE, PGR.Tooltips.Innate, true, PCLCardTagHelper.Delayed);
    public static final PCLCardTagHelper Loyal = new PCLCardTagHelper(PGR.Enums.CardTags.LOYAL, PGR.Tooltips.Loyal, true);
    public static final PCLCardTagHelper Purge = new PCLCardTagHelper(PGR.Enums.CardTags.PURGE, PGR.Tooltips.Purge, false);
    public static final PCLCardTagHelper RetainInfinite = new PCLCardTagHelper(PGR.Enums.CardTags.PCL_RETAIN, PGR.Tooltips.RetainInfinite, true, PCLCardTagHelper.Ethereal);
    public static final PCLCardTagHelper RetainOnce = new PCLCardTagHelper(PGR.Enums.CardTags.PCL_RETAIN_ONCE, PGR.Tooltips.RetainOnce, true, PCLCardTagHelper.RetainInfinite, PCLCardTagHelper.Ethereal);

    public final AbstractCard.CardTags Tag;
    public final PCLCardTagHelper[] PriorityTags;
    public final PCLCardTooltip Tooltip;
    public final boolean IsBuff;

    public PCLCardTagHelper(AbstractCard.CardTags tag, PCLCardTooltip tooltip, boolean isBuff, PCLCardTagHelper... priorityTags)
    {
        this.Tag = tag;
        this.Tooltip = tooltip;
        this.IsBuff = isBuff;
        this.PriorityTags = priorityTags;

        ALL.putIfAbsent(tag, this);
    }

    @Override
    public PCLCardTooltip GetTooltip() {
        return Tooltip;
    }
}
