package pinacolada.stances;

import eatyourbeets.interfaces.delegates.FuncT0;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.interfaces.markers.TooltipObject;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PCLStanceHelper implements TooltipObject
{
    public static final Map<String, PCLStanceHelper> ALL = new HashMap<>();

    public static final PCLStanceHelper MightStance = new PCLStanceHelper(pinacolada.stances.pcl.MightStance.STANCE_ID, PGR.Tooltips.MightStance, PCLAffinity.Red, pinacolada.stances.pcl.MightStance::new);
    public static final PCLStanceHelper VelocityStance = new PCLStanceHelper(pinacolada.stances.pcl.VelocityStance.STANCE_ID, PGR.Tooltips.VelocityStance, PCLAffinity.Green, pinacolada.stances.pcl.VelocityStance::new);
    public static final PCLStanceHelper WisdomStance = new PCLStanceHelper(pinacolada.stances.pcl.WisdomStance.STANCE_ID, PGR.Tooltips.WisdomStance, PCLAffinity.Blue, pinacolada.stances.pcl.WisdomStance::new);
    public static final PCLStanceHelper EnduranceStance = new PCLStanceHelper(pinacolada.stances.pcl.EnduranceStance.STANCE_ID, PGR.Tooltips.EnduranceStance, PCLAffinity.Orange, pinacolada.stances.pcl.EnduranceStance::new);
    public static final PCLStanceHelper InvocationStance = new PCLStanceHelper(pinacolada.stances.pcl.InvocationStance.STANCE_ID, PGR.Tooltips.InvocationStance, PCLAffinity.Light, pinacolada.stances.pcl.InvocationStance::new);
    public static final PCLStanceHelper DesecrationStance = new PCLStanceHelper(pinacolada.stances.pcl.DesecrationStance.STANCE_ID, PGR.Tooltips.DesecrationStance, PCLAffinity.Dark, pinacolada.stances.pcl.DesecrationStance::new);

    public final PCLCardTooltip Tooltip;
    public final PCLAffinity Affinity;
    public final String ID;
    protected final FuncT0<PCLStance> constructor;

    public static PCLStanceHelper Get(String stanceID) {
        return ALL.get(stanceID);
    }

    public static PCLStanceHelper Get(PCLAffinity affinity) {
        return PCLJUtils.Find(ALL.values(), h -> affinity.equals(h.Affinity));
    }

    public static Collection<PCLStanceHelper> Values() {
        return ALL.values().stream().sorted((a, b) -> StringUtils.compare(a.Tooltip.title, b.Tooltip.title)).collect(Collectors.toList());
    }

    public static PCLStanceHelper RandomHelper() {
        return PCLGameUtilities.GetRandomElement(new ArrayList<>(ALL.values()));
    }

    public static PCLStance RandomStance() {
       return RandomHelper().Create();
    }

    public PCLStanceHelper(String stanceID, PCLCardTooltip tooltip, PCLAffinity affinity, FuncT0<PCLStance> constructor)
    {
        this.ID = stanceID;
        this.Tooltip = tooltip;
        this.Affinity = affinity;
        this.constructor = constructor;

        ALL.putIfAbsent(stanceID, this);
    }

    public PCLStance Create()
    {
        if (constructor != null)
        {
            return constructor.Invoke();
        }
        else
        {
            throw new RuntimeException("Do not create a PCLStanceHelper with a null constructor.");
        }
    }

    @Override
    public PCLCardTooltip GetTooltip() {
        return Tooltip;
    }
}
