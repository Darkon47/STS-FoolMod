package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import eatyourbeets.actions.animator.EndPlayerTurnAction;
import eatyourbeets.actions.animator.KillCharacterAction;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamedMoveset.*;
import eatyourbeets.monsters.SharedMoveset.Move_Poison;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.InfinitePower;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.scenes.TheUnnamedReignScene;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class TheUnnamed extends AnimatorMonster
{
    public static final String ID = CreateFullID(TheUnnamed.class.getSimpleName());
    public static final String NAME = "The Unnamed";

    private final Move_Fading moveFading;
    private final Move_Poison movePoison;
    private int stunCounter = 0;

    private final InfinitePower infinitePower;

    public boolean appliedFading = false;
    public int minionsCount = 3;
    public boolean phase2;
    public final AbstractMonster[] minions = new AbstractMonster[3];

    public TheUnnamed()
    {
        super(new Data(ID), EnemyType.BOSS);

        data.SetIdleAnimation(this, 1);

        moveFading = (Move_Fading)
                moveset.AddSpecial(new Move_Fading(5));

        movePoison = (Move_Poison)
                moveset.AddSpecial(new Move_Poison(3));

        moveset.AddSpecial(new Move_SummonDoll());

        if (PlayerStatistics.GetAscensionLevel() >= 4)
        {
            moveset.AddNormal(new Move_Taunt());
            moveset.AddNormal(new Move_SingleAttack(25));
            moveset.AddNormal(new Move_MultiAttack(8, 3));
        }
        else
        {
            moveset.AddNormal(new Move_Taunt());
            moveset.AddNormal(new Move_SingleAttack(20));
            moveset.AddNormal(new Move_MultiAttack(7, 3));
        }

        infinitePower = new InfinitePower(this);
    }

    @Override
    public void die()
    {
        if (!AbstractDungeon.getCurrRoom().cannotLose)
        {
            AbstractDungeon.getCurrRoom().cannotLose = true;

            AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY,
                    1.2f, data.strings.DIALOG[28], this.isPlayer));

            ChangeOverlayColor(new Color(1f, 1f, 1f, 0.3f));

            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();

            CardCrawlGame.stopClock = true;

            RemoveMinions();

            AbstractDungeon.effectsQueue.add(new VictoryEffect());
        }
    }

    @Override
    public void usePreBattleAction()
    {
        if (!PlayerStatistics.SaveData.EnteredUnnamedReign)
        {
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.player.currentHealth = 0;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            return;
        }

        //AbstractDungeon.aiRng.setCounter(AbstractDungeon.aiRng.counter + MathUtils.random(100));

        super.usePreBattleAction();

        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        AbstractDungeon.getCurrRoom().rewards.clear();

        if (AbstractDungeon.player.maxHealth > 400)
        {
            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[1], 4, 4));
            moveFading.fadingTurns = 3;
            moveFading.ExecuteInternal(AbstractDungeon.player);
        }
        else
        {
            CardCrawlGame.music.silenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.playTempBgmInstantly("BOSS_ENDING", true);
            CardCrawlGame.music.updateVolume();
        }

        GameActionsHelper.ApplyPower(this, this, infinitePower);
    }

    @Override
    public void createIntent()
    {
        super.createIntent();

        if (intent == Intent.STUN || intent == Intent.SLEEP)
        {
            if (stunCounter <= 0)
            {
                GameActionsHelper.AddToTop(new TalkAction(this, data.strings.DIALOG[23], 4, 4));
                stunCounter = 1;
            }
            else if (stunCounter == 1)
            {
                GameActionsHelper.AddToTop(new TalkAction(this, data.strings.DIALOG[24], 4, 4));
                stunCounter = 2;
            }
            else if (stunCounter == 2)
            {
                GameActionsHelper.AddToTop(new EndPlayerTurnAction());
                GameActionsHelper.AddToTop(new TalkAction(this, data.strings.DIALOG[25], 4, 4));

                stunCounter = 3;
            }
            else if (stunCounter == 3)
            {
                GameActionsHelper.AddToTop(new EndPlayerTurnAction());
                GameActionsHelper.AddToTop(new TalkAction(this, data.strings.DIALOG[26], 3, 3));

                stunCounter = 4;
            }
            else
            {
                GameActionsHelper.AddToTop(new KillCharacterAction(this, AbstractDungeon.player));
                GameActionsHelper.AddToTop(new TalkAction(this, data.strings.DIALOG[27], 3, 3));
            }
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (!hasPower(InfinitePower.POWER_ID))
        {
            GameActionsHelper.ApplyPowerSilently(this, this, infinitePower, 0);
        }

        if (phase2 && moveFading.CanUse(previousMove))
        {
            if (moveFading.fadingTurns > 2)
            {
                moveFading.fadingTurns -= 1;
            }
            moveFading.SetMove();

            return;
        }

        Move_SummonDoll summonDoll = moveset.GetMove(Move_SummonDoll.class);
        if (summonDoll.CanUse(previousMove))
        {
            summonDoll.SetMove();
            return;
        }

        if (moveset.GetMove(previousMove) instanceof Move_Taunt)
        {
            if (movePoison.poisonAmount > 6)
            {
                movePoison.poisonAmount += 2;
            }
            else
            {
                movePoison.poisonAmount += 1;
            }

            movePoison.SetMove();

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }

    @Override
    public void damage(DamageInfo info)
    {
        // The enchanted armor already ensures no attack can go above 100 damage,
        // this prevents taking extra damage from attacks that do not calculate
        // their damage based on the enemy's powers.
        if (info.output > 100)
        {
            info.output = 100;
        }

        super.damage(info);

        if (this.currentHealth <= 400)
        {
            StartPhase2();
        }
    }

    public void OnDollDeath()
    {
        minionsCount -= 1;
        if (minionsCount <= 0)
        {
            StartPhase2();
        }
    }

    private void StartPhase2()
    {
        if (phase2)
        {
            return;
        }

        phase2 = true;

        GameActionsHelper.VFX(new BorderLongFlashEffect(Color.BLACK, false));
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.scene.fadeInAmbiance();

        ChangeOverlayColor(new Color(0.3f, 0.3f, 0.3f, 0.2f));

        if (!this.isDeadOrEscaped())
        {
            int minions = RemoveMinions();
            int regen = (minions >= 1) ? 110 : 80;
            int plated = (minions >= 2) ? 22 : 16;
            int angry = (minions >= 3) ? 6 : 5;

            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[0], 3, 3));
            GameActionsHelper.ApplyPower(this, this, new RegenPower(this, regen), regen);
            GameActionsHelper.ApplyPower(this, this, new AngryPower(this, angry), angry);
            GameActionsHelper.ApplyPower(this, this, new PlatedArmorPower(this, plated), plated);
            GameActionsHelper.ApplyPower(this, this, new EarthenThornsPower(this, 3), 3);

//            moveFading.SetMove();
//            this.createIntent();

            moveset.GetMove(Move_Taunt.class).disabled = true;
            moveset.AddNormal(movePoison);
            rollMove();
            createIntent();
        }
    }

    private static void ChangeOverlayColor(Color color)
    {
        TheUnnamedReignScene scene = Utilities.SafeCast(AbstractDungeon.scene, TheUnnamedReignScene.class);
        if (scene != null)
        {
            scene.overlayColor = color;
        }
    }

    private boolean deathNoteMessage = false;
    public void TriedUsingDeathNote()
    {
        if (!deathNoteMessage)
        {
            deathNoteMessage = true;
            GameActionsHelper.AddToBottom(new TalkAction(this, data.strings.DIALOG[2], 3, 3));
        }
    }

    private int RemoveMinions()
    {
        int removed = 0;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.hasPower(MinionPower.POWER_ID))
            {
                GameActionsHelper.AddToBottom(new EscapeAction(m));
                removed += 1;
            }
        }

        return removed;
    }

    protected static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = 1000;
            atlasUrl = "images/monsters/animator/TheUnnamed/TheUnnamed.atlas";
            jsonUrl = "images/monsters/animator/TheUnnamed/TheUnnamed.json";

            SetHB(0, -20, 200, 260, 0, 80);
        }
    }

    protected static class VictoryEffect extends AbstractGameEffect
    {
        boolean fastMode;
        WaitRealtimeAction wait;

        public VictoryEffect()
        {
            this.startingDuration = this.duration = 2.5f;
            wait = new WaitRealtimeAction(this.duration);
        }

        @Override
        public void update()
        {
            wait.update();
            if (wait.isDone)
            {
                MapRoomNode cur = AbstractDungeon.currMapNode;

                AbstractDungeon.nextRoom = new MapRoomNode(cur.x, cur.y + 1);
                AbstractDungeon.nextRoom.room = new TrueVictoryRoom();
                AbstractDungeon.nextRoomTransitionStart();

                Settings.FAST_MODE = fastMode;
                this.isDone = true;
            }
        }

        @Override
        public void render(SpriteBatch spriteBatch)
        {

        }

        @Override
        public void dispose()
        {

        }
    }
}
