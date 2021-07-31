package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.actions.orbs.AetherOrbEvokeAction;
import eatyourbeets.actions.orbs.AetherOrbPassiveAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.OrbFlareEffect2;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Aether extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Aether.class);

    public static Texture imgExt1;
    public static Texture imgExt2;
    private final boolean hFlip1;

    public Aether()
    {
        super(ORB_ID, Timing.EndOfTurn);

        if (imgExt1 == null)
        {
            imgExt1 = ImageMaster.loadImage("images/orbs/animator/AirLeft.png");
            imgExt2 = ImageMaster.loadImage("images/orbs/animator/AirRight.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();

        this.baseEvokeAmount = this.evokeAmount = 3;
        this.basePassiveAmount = this.passiveAmount = 4;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription()
    {
        String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, this.evokeAmount);
    }

    @Override
    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play(SFX.POWER_FLIGHT, 0.2f);
        CardCrawlGame.sound.playAV(SFX.ORB_PLASMA_EVOKE, 1.2f, 0.7f);
    }

    @Override
    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
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

        float scaleExt1 = this.bobEffect.y / 88f;
        float scaleExt2 = this.bobEffect.y / 77f;
        float angleExt = this.angle / 12f;

        sb.draw(imgExt1, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, angleExt, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imgExt2, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt2, this.scale + scaleExt2, angleExt, 0, 0, 96, 96, this.hFlip1, false);

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.playAV("ATTACK_WHIRLWIND", 1.5f, 0.7f);
        CardCrawlGame.sound.playAV("ORB_PLASMA_CHANNEL", 1.2f, 0.7f);
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.2f);
    }

    @Override
    public void Evoke()
    {
        super.Evoke();

        GameActions.Top.Add(new AetherOrbEvokeAction(this.evokeAmount));
    }

    @Override
    public void Passive()
    {
        super.Passive();

        GameActions.Bottom.Add(new AetherOrbPassiveAction(this.passiveAmount));
    }

    @Override
    protected OrbFlareEffect2 GetOrbFlareEffect()
    {
        return super.GetOrbFlareEffect().SetColors(Color.FOREST, Color.CYAN);
    }
}