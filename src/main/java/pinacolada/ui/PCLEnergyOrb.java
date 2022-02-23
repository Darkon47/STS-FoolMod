package pinacolada.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.Colors;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLEnergyOrb implements EnergyOrbInterface {
    public static final TextureCache[] DEFAULT_TEXTURES =
            {
                    PGR.PCL.Images.ORB_BASE_LAYER,
                    PGR.PCL.Images.ORB_TOP_LAYER1,
                    PGR.PCL.Images.ORB_TOP_LAYER2,
                    PGR.PCL.Images.ORB_TOP_LAYER3,
                    PGR.PCL.Images.ORB_TOP_LAYER4,
            };
    protected static final TextureCache DEFAULT_FLASH = PGR.PCL.Images.ORB_FLASH;
    protected static final float BASE_MULT = 3f;
    protected static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
    protected static final TextureCache BASE_BORDER = PGR.PCL.Images.ORB_BASE_BORDER;
    protected TextureCache flash;
    protected TextureCache[] images;
    protected float[] angleMults;
    protected float angle;

    public PCLEnergyOrb() {
        this(DEFAULT_TEXTURES, DEFAULT_FLASH, null);
    }

    public PCLEnergyOrb(TextureCache[] images, TextureCache flash) {
        this(images, flash, null);
    }

    public PCLEnergyOrb(TextureCache[] images, TextureCache flash, float[] angleMults) {
        this.images = images;
        this.flash = flash;
        assert images != null && images.length >= 1;
        if (angleMults != null) {
            this.angleMults = angleMults;
            assert angleMults.length == images.length;
        }
        else {
            this.angleMults = new float[images.length];
            float mult = BASE_MULT;
            for (int i = 0; i < this.angleMults.length; i++) {
                this.angleMults[i] = mult;
                mult *= -2;
            }
        }
    }

    @Override
    public void updateOrb(int energyCount) {
        this.angle += energyCount == 0 ? Gdx.graphics.getDeltaTime() * 0.5f : Gdx.graphics.getDeltaTime() * 2;
    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.draw(BASE_BORDER.Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angle, 0, 0, 128, 128, false, false);
        if (enabled) {
            this.renderOrbLayer(sb, current_x, current_y);
        }
        else {
            PCLRenderHelpers.DrawGrayscale(sb, () ->
                    this.renderOrbLayer(sb, current_x, current_y));
        }
    }

    protected void renderOrbLayer(SpriteBatch sb, float current_x, float current_y)
    {
        sb.draw(this.images[0].Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angle * this.angleMults[0], 0, 0, 128, 128, false, false);
        sb.setColor(Colors.White(0.26f));
        PCLRenderHelpers.DrawBlended(sb, PCLRenderHelpers.BlendingMode.Normal, () -> {
            for (int i = 1; i < images.length; i++) {
                sb.draw(this.images[i].Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angle * this.angleMults[i], 0, 0, 128, 128, false, false);
            }
        });
        sb.setColor(Colors.White(0.17f));
        PCLRenderHelpers.DrawBlended(sb, PCLRenderHelpers.BlendingMode.Glowing, () -> {
            for (int i = images.length - 2; i < images.length; i++) {
                sb.draw(this.images[i].Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, -this.angle * this.angleMults[i], 0, 0, 128, 128, false, false);
                sb.draw(this.images[i].Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 64.0F, 64.0F, ORB_IMG_SCALE / 2, ORB_IMG_SCALE / 2, this.angle * this.angleMults[i], 0, 0, 128, 128, false, false);
                sb.draw(this.images[i].Texture(), current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 32, 32, ORB_IMG_SCALE / 4, ORB_IMG_SCALE / 4, -this.angle * this.angleMults[i] + 90f, 0, 0, 128, 128, false, false);
            }
        });
    }

    public Texture getEnergyImage() {
        return this.flash.Texture();
    }
}
