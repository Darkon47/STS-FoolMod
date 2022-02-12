package pinacolada.ui.combat;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.ui.GUIElement;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;

public abstract class PCLAffinityMeter extends GUIElement {
    public abstract PCLAffinity OnMatch(AbstractCard card);
    public abstract PCLAffinity OnNotMatch(AbstractCard card);
    public abstract PCLAffinity GetCurrentAffinity();
    public abstract PCLAffinity Get(int target);
    public abstract PCLAffinity Set(PCLAffinity affinity, int target);
    public abstract void Initialize();
    public abstract void Update(PCLCard card);
    public abstract boolean HasMatch(AbstractCard card);

    public void Flash(int target) {}
    public void OnStartOfTurn() {}
    public final void Update() {} // This should not be called
}
