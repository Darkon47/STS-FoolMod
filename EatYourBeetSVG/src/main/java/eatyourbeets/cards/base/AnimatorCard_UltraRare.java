package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.ultrarare.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.RenderHelpers;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatorCard_UltraRare extends AnimatorCard implements Hidden
{
    private static final Map<String, AnimatorCard_UltraRare> cards = new HashMap<>();
    private static final Color RENDER_COLOR = new Color(0.4f, 0.4f, 0.4f, 1);
    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72};
    private static final String idPrefix = new String(whatever);

    protected AnimatorCard_UltraRare(String id, int cost, CardType type, CardTarget target)
    {
        super(id, cost, type, CardColor.COLORLESS, CardRarity.SPECIAL, target);

        SetUnique(true, false);
    }

    public static Map<String, AnimatorCard_UltraRare> GetCards()
    {
        if (cards.isEmpty())
        {
            cards.put(Chomusuke.ID, new Chomusuke());
            cards.put(Giselle.ID, new Giselle());
            cards.put(Veldora.ID, new Veldora());
            cards.put(Rose.ID, new Rose());
            cards.put(Truth.ID, new Truth());
            cards.put(Azriel.ID, new Azriel());
            cards.put(Hero.ID, new Hero());
            cards.put(SirTouchMe.ID, new SirTouchMe());
            cards.put(ShikizakiKiki.ID, new ShikizakiKiki());
            cards.put(HiiragiTenri.ID, new HiiragiTenri());
            cards.put(JeanneDArc.ID, new JeanneDArc());
            cards.put(NivaLada.ID, new NivaLada());
            cards.put(SeriousSaitama.ID, new SeriousSaitama());
            //Cards.put(Cthulhu.ID, new Cthulhu());
            //Cards.put(InfinitePower.ID, new InfinitePower());
        }

        return cards;
    }

    public static void MarkAsSeen(String cardID)
    {
        if (!IsSeen(cardID))
        {
            UnlockTracker.seenPref.putInteger(cardID, 2);
            UnlockTracker.seenPref.flush();
        }
    }

    public static boolean IsSeen(String cardID)
    {
        return UnlockTracker.seenPref.getInteger(cardID, 0) >= 1;
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        RENDER_COLOR.a = this.transparency;
        switch (type)
        {
            case ATTACK:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_ATTACK_BG_GRAY, x, y);
                break;
            case SKILL:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_SKILL_BG_GRAY, x, y);
                break;
            case POWER:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, ImageMaster.CARD_POWER_BG_GRAY, x, y);
                break;
            default:
                super.renderCardBg(sb, x, y);
                break;
        }
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return IMAGES.CARD_ENERGY_ORB_A.Texture();
    }

    @Override
    protected Texture GetCardBanner()
    {
        return IMAGES.CARD_BANNER_ULTRARARE.Texture();
    }
}