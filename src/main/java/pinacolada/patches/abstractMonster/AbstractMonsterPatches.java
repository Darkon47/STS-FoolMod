package pinacolada.patches.abstractMonster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.pcl.MindControlPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class AbstractMonsterPatches
{
    @SpirePatch(clz = AbstractMonster.class, method = "<class>")
    public static class AbstractMonster_Fields
    {
        public static final SpireField<PCLEnemyIntent> enemyIntent = new SpireField<>(() -> null);
    }

    @SpirePatch(clz= AbstractMonster.class, method = "renderDamageRange", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_Render
    {
        @SpirePrefixPatch
        public static void Method(AbstractMonster __instance, SpriteBatch sb)
        {
            GR.UI.CombatScreen.Intents.RenderMonsterInfo(__instance, sb);
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "renderTip", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_RenderTip
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractMonster __instance, SpriteBatch sb)
        {
            if (__instance.reticleAlpha == 0)
            {
                PCLCardTooltip.QueueTooltips(__instance);
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GameActionManager_GetNextAction {
        public GameActionManager_GetNextAction() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && m.getMethodName().equals("takeTurn")) {
                        m.replace("if (pinacolada.powers.PCLCombatStats.OnMonsterMove(m)) {$_ = $proceed($$);}");
                    }
                }
            };
        }


        // TODO remove
        public static void Use(AbstractMonster m)
        {
            boolean canAct = true;
            for (AbstractPower p : m.powers) {
                MindControlPower mp = PCLJUtils.SafeCast(p, MindControlPower.class);
                if (mp != null && mp.active)
                {
                    canAct = canAct & mp.DoActions();
                }
            }
            if (canAct) {
                m.takeTurn();
            }
        }
    }
}
