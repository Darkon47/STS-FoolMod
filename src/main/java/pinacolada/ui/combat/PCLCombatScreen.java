package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.GUIElement;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

public class PCLCombatScreen extends GUIElement implements OnStatsClearedSubscriber, OnBattleStartSubscriber
{
    public final EnemySubIntents Intents = new EnemySubIntents();
    public final CombatHelper Helper = new CombatHelper();

    protected float delay = 0;

    public PCLCombatScreen()
    {
        PCLCombatStats.onStatsCleared.Subscribe(this);
        PCLCombatStats.onBattleStart.Subscribe(this);
        SetActive(false);
    }

    //@Formatter: off
    @Override public void OnBattleStart() { OnStatsCleared(); }
    @Override public void OnStatsCleared()
    {
        SetActive(PCLGameUtilities.InBattle() && GR.PCL.IsSelected());
        PCLCombatStats.onBattleStart.Subscribe(this);
        PCLCombatStats.MatchingSystem.SetActive(isActive);
        Intents.Clear();
        Helper.Clear();
    }
    //@Formatter: on

    @Override
    public void Update()
    {
        PCLCombatStats.MatchingSystem.TryUpdate();
        PCLCombatStats.ControlPile.Update();
        CombatStats.Dolls.TryUpdate();
        Helper.Update();

    }

    @Override
    public void Render(SpriteBatch sb)
    {
        PCLCombatStats.MatchingSystem.TryRender(sb);
        PCLCombatStats.ControlPile.Render(sb);
        for (UnnamedDoll doll : CombatStats.Dolls.Slots) {
            if (doll != null && doll.Visible) {
                GR.UI.AddPreRender(doll::render);
            }
        }
    }
}
