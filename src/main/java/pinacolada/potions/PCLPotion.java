package pinacolada.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.FlashPotionEffect;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.MethodInfo;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import javax.xml.transform.Result;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class PCLPotion extends AbstractPotion
{
    protected static final MethodInfo.T0<Result> _updateFlash = PCLJUtils.GetMethod("updateFlash", AbstractPotion.class);
    protected static final MethodInfo.T0<Result> _updateEffect = PCLJUtils.GetMethod("updateEffect", AbstractPotion.class);
    protected static final FieldInfo<Float> _angle = PCLJUtils.GetField("angle", AbstractPotion.class);
    protected static final FieldInfo<ArrayList<FlashPotionEffect>> _effect = PCLJUtils.GetField("effect", AbstractPotion.class);
    protected static final FieldInfo<Texture> _containerImg = PCLJUtils.GetField("containerImg", AbstractPotion.class);
    protected static final FieldInfo<Texture> _liquidImg = PCLJUtils.GetField("liquidImg", AbstractPotion.class);
    protected static final FieldInfo<Texture> _hybridImg = PCLJUtils.GetField("hybridImg", AbstractPotion.class);
    protected static final FieldInfo<Texture> _spotsImg = PCLJUtils.GetField("spotsImg", AbstractPotion.class);
    protected static final FieldInfo<Texture> _outlineImg = PCLJUtils.GetField("outlineImg", AbstractPotion.class);

    public static String CreateFullID(Class<? extends PCLPotion> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public final String[] DESCRIPTIONS;
    public final ArrayList<PCLCardTooltip> pclTips = new ArrayList<>();

    // We deliberately avoid using initializeData because we need to load the PotionStrings after the super call
    public PCLPotion(String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor)
    {
        super("", id, rarity, size, effect, liquidColor.cpy(), hybridColor.cpy(), spotsColor.cpy());
        PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(id);
        name = potionStrings.NAME;
        DESCRIPTIONS = potionStrings.DESCRIPTIONS;
        this.potency = this.getPotency();
        this.description = PCLJUtils.Format(DESCRIPTIONS[0], this.potency);
        this.isThrown = false;
        initializeTips();
    }

    @Override
    public AbstractPotion makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            PCLJUtils.LogError(this, e.getMessage());
            return null;
        }
    }

    @Override
    public void labRender(SpriteBatch sb)
    {
        renderImpl(sb, true);
    }

    @Override
    public void shopRender(SpriteBatch sb)
    {
        this.generateSparkles(0.0F, 0.0F, false);
        renderImpl(sb, false);
    }

    protected void initializeTips() {
        pclTips.clear();
        pclTips.add(new PCLCardTooltip(name, description, GR.Enums.Characters.THE_FOOL));
        PCLGameUtilities.ScanForTips(description, pclTips);
    }

    protected void renderImpl(SpriteBatch sb, boolean useOutlineColor) {
        float angle = _angle.Get(this);
        Texture containerImg = _containerImg.Get(this);
        Texture liquidImg = _liquidImg.Get(this);
        Texture hybridImg = _hybridImg.Get(this);
        Texture spotsImg = _spotsImg.Get(this);
        Texture outlineImg = _outlineImg.Get(this);

        _updateFlash.Invoke(this);
        _updateEffect.Invoke(this);
        if (this.hb.hovered) {
            PCLCardTooltip.QueueTooltips(this);
            this.scale = 1.5F * Settings.scale;
        } else {
            this.scale = MathHelper.scaleLerpSnap(this.scale, 1.2F * Settings.scale);
        }

        this.renderOutline(sb, useOutlineColor ? this.labOutlineColor : Settings.HALF_TRANSPARENT_BLACK_COLOR);
        sb.setColor(this.liquidColor);
        sb.draw(liquidImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, angle, 0, 0, 64, 64, false, false);
        if (this.hybridColor != null) {
            sb.setColor(this.hybridColor);
            sb.draw(hybridImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, angle, 0, 0, 64, 64, false, false);
        }

        if (this.spotsColor != null) {
            sb.setColor(this.spotsColor);
            sb.draw(spotsImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, angle, 0, 0, 64, 64, false, false);
        }

        sb.setColor(Color.WHITE);
        sb.draw(containerImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, angle, 0, 0, 64, 64, false, false);

        for (FlashPotionEffect e : _effect.Get(this)) {
            e.render(sb, this.posX, this.posY);
        }

        if (this.hb != null) {
            this.hb.render(sb);
        }
    }

}
