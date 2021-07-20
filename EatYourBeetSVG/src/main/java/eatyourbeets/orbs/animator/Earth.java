package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.orbs.EarthOrbEvokeAction;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import patches.orbs.AbstractOrbPatches;

public class Earth extends AnimatorOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);

    public static Texture img;

    private final boolean hFlip1;
    private final boolean hFlip2;

    private boolean evoked;
    private int turns;

    public Earth()
    {
        super(ORB_ID, true);

        if (img == null)
        {
            img = ImageMaster.loadImage("images/orbs/animator/Earth.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();
        this.hFlip2 = MathUtils.randomBoolean();
        this.evoked = false;
        this.baseEvokeAmount = 16;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 2;
        this.passiveAmount = this.basePassiveAmount;
        this.channelAnimTimer = 0.5f;
        this.turns = 3;
        this.updateDescription();
    }

    public void updateDescription()
    {
        final String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = desc[0] + this.passiveAmount + desc[1] + this.evokeAmount + desc[2] + this.turns + desc[3];
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (!AbstractDungeon.player.orbs.contains(this))
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            return;
        }

        this.turns -= 1;

        if (turns <= 0)
        {
            GameActions.Bottom.Add(new EvokeSpecificOrbAction(this));

            evoked = true;
        }
    }

    @Override
    public void onEndOfTurn()
    {
        if (evoked)
        {
            return;
        }

        if (turns > 0)
        {
            PassiveEffect();
        }

        this.updateDescription();
    }

    @Override
    public void triggerEvokeAnimation()
    {
        //CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_CHANNEL", 0.1f);
        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1f);
        GameEffects.Queue.Add(VFX.Rock(this.hb));
    }

    @Override
    public void applyFocus()
    {
        int focus = GetFocus();
        if (focus > 0)
        {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + focus);
            this.evokeAmount = Math.max(0, this.baseEvokeAmount + (focus * 2));
        }
        else
        {
            this.evokeAmount = this.baseEvokeAmount;
            this.passiveAmount = this.basePassiveAmount;
        }

        AbstractOrbPatches.ApplyAmountChangeToOrb(this);
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 180f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);
        sb.draw(img, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale, this.scale, this.angle / 12f, 0, 0, 96, 96, this.hFlip1, false);
        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb)
    {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET - 4f * Settings.scale, new Color(0.2f, 1f, 1f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, new Color(0.8f, 0.7f, 0.2f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turns), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, this.c, this.fontScale);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_CHANNEL", 0.2f);
        turns = 3;
        evoked = false;
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        Earth copy = new Earth();
        if (!evoked)
        {
            copy.turns = turns;
        }

        return copy;
    }

    @Override
    public void PassiveEffect()
    {
        GameActions.Bottom.Add(new EarthOrbPassiveAction(passiveAmount));
        super.PassiveEffect();
    }

    @Override
    public void EvokeEffect()
    {
        if (evokeAmount > 0)
        {
            GameActions.Top.Add(new EarthOrbEvokeAction(evokeAmount));
            super.EvokeEffect();
        }

        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;
    }
}