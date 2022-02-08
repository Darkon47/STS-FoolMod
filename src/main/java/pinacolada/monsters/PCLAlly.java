package pinacolada.monsters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import pinacolada.powers.special.ChangeIntentPower;

import java.util.HashMap;
import java.util.UUID;

//TODO Make independent of UnnamedDoll
public class PCLAlly extends UnnamedDoll {
    public final UUID uuid;
    public HashMap<Intent, ChangeIntentPower> specialMoves = new HashMap<>();

    public PCLAlly(UnnamedDoll.SummonData summonData) {
        super(summonData);
        uuid = UUID.randomUUID();
    }

    public void SwitchIntent(Intent targetIntent) {
        ChangeIntentPower move = this.specialMoves.get(targetIntent);
        if (move != null) {
            move.Select(true);
        }
        else {
            super.SwitchIntent(targetIntent);
        }
    }

    public void update() {
        super.update();

        int i = 0;
        for (ChangeIntentPower po : specialMoves.values()) {
            po.update(i);
            i++;
        }
    }

    public void render(SpriteBatch sb) {
        super.render(sb);

        //for (ChangeIntentPower po : specialMoves.values()) {
        //    po.renderIcons(sb, po.rhb.x, po.rhb.y, Colors.White(1.0f));
        //}
    }
}
