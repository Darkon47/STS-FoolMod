package pinacolada.actions.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import eatyourbeets.actions.EYBAction;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class DieAction extends EYBAction
{
    public DieAction(AbstractCreature target)
    {
        super(ActionType.DAMAGE, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(target, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!PCLGameUtilities.IsDeadOrEscaped(target))
        {
            AbstractMonster m = PCLJUtils.SafeCast(target, AbstractMonster.class);
            if (m != null)
            {
                m.currentHealth = 0;
                m.die();
                m.hideHealthBar();

                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
                {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    PCLGameEffects.List.Add(new DeckPoofEffect(64f * Settings.scale, 64f * Settings.scale, true));
                    PCLGameEffects.List.Add(new DeckPoofEffect((float) Settings.WIDTH - 64f * Settings.scale, 64f * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }
            }
            else if (target instanceof AbstractPlayer)
            {
                player.isDead = true;
                player.currentHealth = 0;
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            }
        }
    }
}
