package eatyourbeets.relics.animator;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import eatyourbeets.interfaces.OnRelicObtainedSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.monsters.Bosses.KrulTepes;

public class ExquisiteBloodVial extends AnimatorRelic implements OnRelicObtainedSubscriber
{
    private static final int HEAL_AMOUNT = 2;

    public static final String ID = CreateFullID(ExquisiteBloodVial.class.getSimpleName());

    public boolean truePotential;

    private int regenAmount;
    private int maxHPAmount;

    public ExquisiteBloodVial()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (trigger == OnRelicObtainedSubscriber.Trigger.Equip && relic instanceof BloodVial && truePotential)
        {
            GameEffects.Queue.Add(new RemoveRelicEffect(this, relic, 1));
        }
    }

    public String getUpdatedDescription()
    {
        if (counter < 0)
        {
            return JavaUtilities.Format(this.DESCRIPTIONS[0], HEAL_AMOUNT);
        }
        else
        {
            return JavaUtilities.Format(this.DESCRIPTIONS[1], regenAmount, maxHPAmount);
        }
    }

    @Override
    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (truePotential)
        {
            GameActions.Bottom.ApplyPower(p, p, new RegenPower(p, regenAmount), regenAmount);
            p.increaseMaxHp(maxHPAmount, true);
        }
        else
        {
            GameActions.Top.Add(new RelicAboveCreatureAction(p, this));
            GameActions.Top.Heal(HEAL_AMOUNT);
        }

        this.flash();
    }

    @Override
    public void update()
    {
        super.update();

        if (truePotential || AbstractDungeon.currMapNode == null)
        {
            return;
        }

        if (counter > 0)
        {
            UnlockPotential();
            Refresh();
        }
        else if (HitboxRightClick.rightClicked.get(this.hb))
        {
            RestRoom room = JavaUtilities.SafeCast(AbstractDungeon.getCurrRoom(), RestRoom.class);
            if (room != null && room.event == null)
            {
                MapRoomNode cur = AbstractDungeon.currMapNode;

                cur.room = new MonsterRoom();
                cur.room.rewardAllowed = false;
                cur.room.rewards.clear();
                cur.room.monsters = new MonsterGroup(new KrulTepes());
                cur.room.monsters.init();

                AbstractDungeon.currMapNode = new MapRoomNode(cur.x, cur.y - 1);
                AbstractDungeon.currMapNode.room = room;
                AbstractDungeon.nextRoom = cur;
                AbstractDungeon.lastCombatMetricKey = KrulTepes.ID;
                AbstractDungeon.nextRoomTransitionStart();
            }
        }
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        Refresh();
    }

    public void Refresh()
    {
        this.regenAmount = this.counter;
        this.maxHPAmount = 1 + (this.counter / 3);

        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void UnlockPotential()
    {
        if (!truePotential)
        {
            this.truePotential = true;
            if (counter < 0)
            {
                this.counter = 0;
            }

            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic relic : p.relics)
            {
                if (relic instanceof BloodVial)
                {
                    AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(this, relic, 1));
                }
            }
        }
    }

//    @Override
//    public Integer onSave()
//    {
//        return counter;
//    }
//
//    @Override
//    public void onLoad(Integer value)
//    {
//        logger.info("ONLOAD: " + value);
//        if (value == null || value < 0)
//        {
//            savedCounter = -1;
//            counter = savedCounter;
//            logger.info("ONLOAD 1: " + counter);
//        }
//        else
//        {
//            savedCounter = value;
//            counter = savedCounter;
//            logger.info("ONLOAD 2: " + counter);
//            UnlockPotential();
//            Refresh();
//        }
//    }
}