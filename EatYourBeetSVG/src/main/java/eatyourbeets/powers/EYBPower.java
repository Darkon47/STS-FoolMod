package eatyourbeets.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.powers.EYBFlashPowerEffect;
import eatyourbeets.effects.powers.EYBGainPowerEffect;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class EYBPower extends AbstractPower implements CloneablePowerInterface
{
    protected static final FieldInfo<ArrayList<AbstractGameEffect>> _effect = JUtils.GetField("effect", AbstractPower.class);
    protected static final float ICON_SIZE = 32f;

    protected static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);
    protected final ArrayList<AbstractGameEffect> effects;
    protected final PowerStrings powerStrings;

    public static AbstractPlayer player = null;
    public static Random rng = null;
    public TextureAtlas.AtlasRegion powerIcon;
    public boolean hideAmount = false;
    public boolean enabled = true;
    public int maxAmount = 9999;
    public int baseAmount = 0;

    public static String DeriveID(String base)
    {
        return base + "Power";
    }

    protected EYBPower(AbstractCreature owner, EYBCardData cardData, EYBRelic relic)
    {
        this.effects = _effect.Get(this);
        this.owner = owner;
        this.img = null;
        this.powerStrings = new PowerStrings();

        if (relic != null)
        {
            this.ID = DeriveID(relic.relicId);
            this.powerIcon = relic.GetPowerIcon();
            this.powerStrings.NAME = relic.name;
            this.powerStrings.DESCRIPTIONS = relic.DESCRIPTIONS;
        }
        else
        {
            this.ID = DeriveID(cardData.ID);
            this.powerIcon = cardData.GetCardIcon();
            this.powerStrings.NAME = cardData.Strings.NAME;
            this.powerStrings.DESCRIPTIONS = cardData.Strings.EXTENDED_DESCRIPTION;
        }

        this.name = powerStrings.NAME;
    }

    public EYBPower(AbstractCreature owner, EYBRelic relic)
    {
        this(owner, null, relic);
    }

    public EYBPower(AbstractCreature owner, EYBCardData cardData)
    {
        this(owner, cardData, null);
    }

    public EYBPower(AbstractCreature owner, String id)
    {
        this.effects = _effect.Get(this);
        this.owner = owner;
        this.ID = id;

        final String imagePath = GR.GetPowerImage(ID);
        if (Gdx.files.internal(imagePath).exists())
        {
            this.img = GR.GetTexture(imagePath);
        }

        this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(this.ID);
        this.name = powerStrings.NAME;
    }

    protected void Initialize(int amount)
    {
        Initialize(amount, PowerType.BUFF, false);
    }

    protected void Initialize(int amount, PowerType type, boolean turnBased)
    {
        this.baseAmount = this.amount = Mathf.Min(9999, amount);
        this.type = type;
        this.isTurnBased = turnBased;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        if ((baseAmount += stackAmount) > maxAmount)
        {
            baseAmount = maxAmount;
        }
        if ((amount + stackAmount) > maxAmount)
        {
            stackAmount = maxAmount - amount;
        }

        super.stackPower(stackAmount);
    }

    @Override
    public void updateDescription()
    {
        switch (powerStrings.DESCRIPTIONS.length)
        {
            case 0:
            {
                JUtils.LogError(this, "powerStrings.Description was an empty array, " + this.name);
                break;
            }

            case 1:
            {
                this.description = FormatDescription(0, amount);
                break;
            }

            case 2:
            {
                this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
                break;
            }

            default:
            {
                StringJoiner stringJoiner = new StringJoiner(Integer.toString(this.amount));
                for (String s : powerStrings.DESCRIPTIONS)
                {
                    stringJoiner.add(s);
                }
                this.description = stringJoiner.toString();
            }
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        try
        {
            return this.getClass().getDeclaredConstructor(AbstractCreature.class, int.class).newInstance(owner, amount);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1)
        {
            try
            {
                return this.getClass().getDeclaredConstructor(AbstractCreature.class).newInstance(owner);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2)
            {
                try
                {
                    return this.getClass().getConstructor().newInstance();
                }
                catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e3)
                {
                    return null;
                }
            }
        }
    }

    public void ReducePower(int amount)
    {
        GameActions.Bottom.ReducePower(this, amount);
    }

    public void RemovePower()
    {
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    public int ResetAmount()
    {
        this.amount = baseAmount;
        updateDescription();
        return amount;
    }

    public EYBPower SetEnabled(boolean enable)
    {
        this.enabled = enable;

        return this;
    }

    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster enemy) {
        return super.atDamageFinalGive(damage,type,card);
    }

    @Override
    public void flash()
    {
        this.effects.add(new EYBGainPowerEffect(this, true));
        GameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void flashWithoutSound()
    {
        this.effects.add(new EYBGainPowerEffect(this, false));
        GameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (this.powerIcon != null)
        {
            RenderHelpers.DrawCentered(sb, enabled ? c : disabledColor, this.powerIcon, x, y, ICON_SIZE, ICON_SIZE, 1, 0);
        }
        else
        {
            sb.setColor(enabled ? c : disabledColor);
            sb.draw(this.img, x - 12f, y - 12f, 16f, 16f, ICON_SIZE, ICON_SIZE, Settings.scale * 1.5f, Settings.scale * 1.5f, 0f, 0, 0, 32, 32, false, false);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (hideAmount)
        {
            return;
        }

        ColoredString amount = GetPrimaryAmount(c);
        if (amount != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount.text, x, y, fontScale, amount.color);
        }

        ColoredString amount2 = GetSecondaryAmount(c);
        if (amount2 != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount2.text, x, y + 15f * Settings.scale, 1, amount2.color);
        }
    }

    protected String FormatDescription(int index, Object... args)
    {
        return JUtils.Format(powerStrings.DESCRIPTIONS[index], args);
    }

    protected ColoredString GetPrimaryAmount(Color c)
    {
        if (this.amount > 0)
        {
            if (this.isTurnBased)
            {
                return new ColoredString(amount, Color.WHITE, c.a);
            }
            else
            {
                return new ColoredString(amount, Color.GREEN, c.a);
            }
        }
        else if (this.amount < 0 && this.canGoNegative)
        {
            return new ColoredString(amount, Color.RED, c.a);
        }
        else
        {
            return null;
        }
    }

    protected ColoredString GetSecondaryAmount(Color c)
    {
        return null;
    }
}