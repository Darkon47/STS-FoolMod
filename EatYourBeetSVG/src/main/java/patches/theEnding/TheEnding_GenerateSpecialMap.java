package patches.theEnding;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import eatyourbeets.events.animator.TheMaskedTraveler2;
import eatyourbeets.resources.GR;
import eatyourbeets.rooms.AnimatorCustomEventRoom;

// Add the event to enter Act 5
@SpirePatch(clz = TheEnding.class, method = "generateSpecialMap")
public class TheEnding_GenerateSpecialMap
{
    @SpirePostfixPatch
    public static void Postfix(TheEnding __instance)
    {
        if (GR.Animator.Data.SpecialTrophies.Trophy1 > 0 || AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
        {
            MapRoomNode rest = __instance.getMap().get(0).get(3);
            MapRoomNode shop = __instance.getMap().get(1).get(3);
            MapRoomNode node = __instance.getMap().get(1).get(5);

            node.room = new AnimatorCustomEventRoom(TheMaskedTraveler2::new);
            node.room.setMapImg(GR.Common.Images.UnnamedReignEntrance.Texture(), GR.Common.Images.UnnamedReignEntranceOutline.Texture());

            connectNode(rest, node);
            connectNode(node, shop);
        }
    }

    private static void connectNode(MapRoomNode src, MapRoomNode dst)
    {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }
}