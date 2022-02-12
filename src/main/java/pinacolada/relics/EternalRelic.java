package pinacolada.relics;

import com.badlogic.gdx.graphics.Texture;
import pinacolada.resources.PGR;

public class EternalRelic extends PCLRelic {
    public static String CreateFullID(Class<? extends PCLRelic> type)
    {
        return PGR.Eternal.CreateID(type.getSimpleName());
    }

    protected EternalRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public EternalRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, PGR.GetTexture(PGR.GetRelicImage(imageID)), tier, sfx);
    }

    public EternalRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, id, tier, sfx);
    }
}
