package pinacolada.interfaces.markers;

import pinacolada.cards.base.PCLCardTooltip;

// Marker used to denote objects that have a PCLCardTooltip
public interface TooltipObject {
    public abstract PCLCardTooltip GetTooltip();
}
