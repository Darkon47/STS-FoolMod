package pinacolada.orbs.pcl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.effects.Projectile;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnFirstSubscriber;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.effects.PCLProjectile;
import pinacolada.effects.SFX;
import pinacolada.orbs.PCLOrb;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Chaos extends PCLOrb implements OnEndOfTurnFirstSubscriber, OnAfterCardPlayedSubscriber
{
    public static final String ORB_ID = CreateFullID(Chaos.class);

    public static TextureCache img = IMAGES.Chaos1;
    private static final TextureCache[] orbitals = { IMAGES.Chaos2, IMAGES.Chaos3, IMAGES.Chaos4};

    private final boolean hFlip1;
    private AbstractOrb currentForm;

    public final ArrayList<Projectile> projectiles = new ArrayList<>();

    public Chaos()
    {
        super(ORB_ID, Timing.StartOfTurnPostDraw, false, false);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = 1;
        this.basePassiveAmount = this.passiveAmount = 1;
        this.currentForm = this;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
        for (TextureCache orbital : orbitals)
        {
            float r = MathUtils.random(0, 10f) * 36f;
            float vX = MathUtils.cosDeg(r);
            float vY = MathUtils.sinDeg(r);
            float x = 2 * hb.width * vX;
            float y = 2 * hb.height * vY;
            projectiles.add(new PCLProjectile(orbital.Texture(), IMAGE_SIZE * 0.8f, IMAGE_SIZE * 0.8f)
                    .SetPosition(cX, cY)
                    .SetColor(Colors.Random(0.8f, 1f, false))
                    .SetScale(0.08f)
                    .SetTargetScale(MathUtils.random(0.5f, 0.8f), null)
                    .SetFlip(MathUtils.randomBoolean(), MathUtils.randomBoolean())
                    .SetOffset(0f, 0f, MathUtils.random(0f, 360f), null)
                    .SetTargetOffset(x, y, null, null)
                    .SetSpeed(vX, vY, MathUtils.random(64f, 100f), null));
        }
    }

    @Override
    public void onChannel()
    {
        super.onChannel();

        PCLCombatStats.onEndOfTurn.Subscribe(this);
        PCLCombatStats.onAfterCardPlayed.Subscribe(this);
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
            int index = orbs.indexOf(currentForm);
            if (index >= 0)
            {
                AbstractOrb orb;
                String id = currentForm.ID;
                do
                {
                    orb = PCLOrbHelper.RandomOrb();
                }
                while (orb.ID.equals(id));

                orb.cX = orb.tX = currentForm.tX;
                orb.cY = orb.tY = currentForm.tY;

                currentForm = orb;
                currentForm.setSlot(index, orbs.size());
                currentForm.playChannelSFX();
                if (currentForm instanceof PCLOrb)
                {
                    ((PCLOrb)currentForm).onChannel();
                }
                orbs.set(index, currentForm);
                AbstractDungeon.onModifyPower();
            }
        }
    }

    @Override
    public void OnEndOfTurnFirst(boolean isPlayer)
    {
        PCLCombatStats.onEndOfTurn.Unsubscribe(this);
        PCLCombatStats.onAfterCardPlayed.Unsubscribe(this);
    }


    @Override
    public void updateAnimation()
    {
        super.updateAnimation();

        final float delta = GR.UI.Delta();
        this.angle += delta * 60;
        for (Projectile texture : projectiles)
        {

            texture.SetPosition(cX, cY)
                    .SetTargetRotation(angle, null)
                    .SetSpeed(texture.target_offset.x * MathUtils.sinDeg(angle * 2) / 2, texture.target_offset.y * MathUtils.cosDeg(angle * 2) / 2, null, null)
                    .Update(delta);
        }

    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float by = bobEffect.y;

        sb.setBlendFunction(770, 1);
        this.shineColor.a = Interpolation.sine.apply(0.4f,0.7f, angle / 95);
        sb.setColor(this.shineColor);
        float scale1 = Interpolation.sine.apply(0.8f,1f, angle / 105);
        sb.draw(img.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale1, scale1, angle, 0, 0, 96, 96, hFlip1, false);
        this.shineColor.a = Interpolation.sine.apply(0.4f,0.7f, -angle / 65);
        sb.setColor(this.shineColor);
        float scale2 = Interpolation.sine.apply(0.8f,1f, -angle / 125);
        sb.draw(img.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale2, scale2, -angle, 0, 0, 96, 96, !hFlip1, false);
        sb.setColor(this.c);

        for (Projectile projectile : projectiles)
        {
            projectile.Render(sb, Colors.Copy(projectile.color, c.a));
        }

        sb.setBlendFunction(770, 771);

        //this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        playChannelSFX(0.93f);
    }

    public void playChannelSFX(float volume)
    {
        SFX.Play(SFX.ORB_LIGHTNING_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_DARK_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_PLASMA_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ATTACK_FIRE, 0.3f, 1.3f, volume);
    }

    @Override
    public void Evoke()
    {
        if (currentForm == this)
        {
            super.Evoke();

            ExecuteEffect();
        }
    }

    @Override
    public void Passive()
    {
        if (currentForm == this)
        {
            super.Passive();

            playChannelSFX(0.85f);
            PCLActions.Last.Callback(this::ExecuteEffect);
        }
    }

    private void ExecuteEffect()
    {
        final RandomizedList<AbstractCard> toDecrease = new RandomizedList<>();
        final RandomizedList<AbstractCard> toIncrease = new RandomizedList<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.costForTurn >= 0)
            {
                toIncrease.Add(c);

                if (c.costForTurn > 0)
                {
                    toDecrease.Add(c);
                }
            }
        }

        if (toDecrease.Size() > 0)
        {
            final AbstractCard c = toDecrease.Retrieve(PCLGameUtilities.GetRNG(), true);
            PCLGameUtilities.ModifyCostForTurn(c, -1, true);
            PCLGameUtilities.Flash(c, true);
            toIncrease.Remove(c);
        }

        if (toIncrease.Size() > 0)
        {
            final AbstractCard c = toIncrease.Retrieve(PCLGameUtilities.GetRNG(), true);
            PCLGameUtilities.ModifyCostForTurn(c, +1, true);
            PCLGameUtilities.Flash(c, Colors.Red(1), true);
        }
    }

    @Override
    protected Color GetColor1()
    {
        return Color.PURPLE;
    }

    @Override
    protected Color GetColor2()
    {
        return Color.GREEN;
    }
}