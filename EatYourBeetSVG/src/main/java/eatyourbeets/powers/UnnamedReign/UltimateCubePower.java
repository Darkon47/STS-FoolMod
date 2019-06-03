package eatyourbeets.powers.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.powers.AnimatorPower;

import java.util.function.Consumer;

public class UltimateCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCubePower.class.getSimpleName());

    private static final int BUFFS_AMOUNT = 1;
    private static final int EXPLOSION_DAMAGE = 140;

    private final RandomizedList<Consumer<AbstractCreature>> buffs = new RandomizedList<>();

    public UltimateCubePower(AbstractCreature owner, int countDown)
    {
        super(owner, POWER_ID);

        amount = countDown;

        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + BUFFS_AMOUNT + desc[1] + amount + desc[2] + EXPLOSION_DAMAGE + desc[3];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (int i = 0; i < BUFFS_AMOUNT; i++)
        {
            GainRandomBuff(owner);
        }
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        if (amount > 0)
        {
            this.amount -= 1;
            updateDescription();
        }
        else
        {
            Explode();
        }
    }

    private void Explode()
    {
        AbstractPlayer p = AbstractDungeon.player;

        int damageStep = EXPLOSION_DAMAGE / 20;
        for (int i = 0; i < 20; i++)
        {
            float x = owner.hb.cX + AbstractDungeon.miscRng.random(-40, 40);
            float y = owner.hb.cY + AbstractDungeon.miscRng.random(-40, 40);
            GameActionsHelper.AddToBottom(new WaitAction(0.3f));
            GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(x, y), 0F));
            GameActionsHelper.DamageTarget(owner, p, damageStep, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
        }

        GameActionsHelper.AddToBottom(new SuicideAction((AbstractMonster)this.owner));
    }

    private void GainRandomBuff(AbstractCreature c)
    {
        if (buffs.Count() < 5)
        {
            buffs.Add(this::BuffHealing);
            buffs.Add(this::BuffDark);
            buffs.Add(this::BuffFire);
            buffs.Add(this::BuffFrost);
            buffs.Add(this::BuffLightning);
        }

        buffs.Retrieve(AbstractDungeon.miscRng).accept(c);
    }

    private void BuffHealing(AbstractCreature c)
    {
        GameActionsHelper.ApplyPower(c, c, new HealingCubePower(c, 4), 4);
    }

    private void BuffFire(AbstractCreature c)
    {
        GameActionsHelper.ApplyPower(c, c, new FireCubePower(c, 3), 3);
    }

    private void BuffFrost(AbstractCreature c)
    {
        GameActionsHelper.ApplyPower(c, c, new FrostCubePower(c, 3), 3);
    }

    private void BuffDark(AbstractCreature c)
    {
        GameActionsHelper.ApplyPower(c, c, new DarkCubePower(c, 3), 3);
    }

    private void BuffLightning(AbstractCreature c)
    {
        GameActionsHelper.ApplyPower(c, c, new LightningCubePower(c, 3), 3);
    }
}