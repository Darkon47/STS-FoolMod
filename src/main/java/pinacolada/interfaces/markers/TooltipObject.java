package pinacolada.interfaces.markers;

import pinacolada.cards.base.PCLCardTooltip;

// Marker used to designate a card or relic that is meant to substitute for an existing one
// Replacement relics cannot be viewed in the compendium but can be obtained via the console
public interface TooltipObject {
    public abstract PCLCardTooltip GetTooltip();
}
