package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.rooms.*;
import eatyourbeets.rooms.AnimatorCustomEventRoom;
import pinacolada.events.pcl.TheMysteriousPeak;

public class BountyMap extends AbstractBountyMap
{
    public static final String ID = CreateFullID(BountyMap.class);

    public BountyMap()
    {
        super(ID);
    }

    @Override
    protected Class<? extends AbstractRoom> GetCurrentRequiredRoom() {
        switch (counter) {
            case 0:
                return MonsterRoom.class;
            case 1:
                return MonsterRoomElite.class;
            case 2:
                return TreasureRoom.class;
            case 3:
                return ShopRoom.class;
            default:
                return EventRoom.class;
        }
    }

    @Override
    protected AnimatorCustomEventRoom.GetEvent GetEventConstructor() {
        return TheMysteriousPeak::new;
    }
}