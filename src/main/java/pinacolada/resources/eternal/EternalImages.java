package pinacolada.resources.eternal;

import eatyourbeets.ui.TextureCache;

public class EternalImages {
    public final String CHARACTER_PNG = "images/eternal/characters/idle/animator.png";
    public final String SKELETON_ATLAS = "images/eternal/characters/idle/animator.atlas";
    public final String SKELETON_JSON = "images/eternal/characters/idle/animator.json";
    public final String SHOULDER1_PNG = "images/eternal/characters/shoulder.png";
    public final String SHOULDER2_PNG = "images/eternal/characters/shoulder2.png";
    public final String CORPSE_PNG = "images/eternal/characters/corpse.png";
    public final String CHAR_BUTTON_PNG = "images/eternal/ui/charselect/button.png";
    public final String CHAR_BACKGROUND = "images/eternal/ui/charselect/background.png";

    public final String ATTACK_PNG = "images/eternal/cardui/512/bg_attack_canvas.png";
    public final String SKILL_PNG = "images/eternal/cardui/512/bg_skill_canvas.png";
    public final String POWER_PNG = "images/eternal/cardui/512/bg_power_canvas.png";
    public final String ATTACK_PNG_L = "images/eternal/cardui/1024/bg_attack_canvas.png";
    public final String SKILL_PNG_L = "images/eternal/cardui/1024/bg_skill_canvas.png";
    public final String POWER_PNG_L = "images/eternal/cardui/1024/bg_power_canvas.png";
    public final String ORB_A_PNG = "images/eternal/cardui/512/energy_orb_default_a.png";
    public final String ORB_B_PNG = "images/eternal/cardui/512/energy_orb_default_b.png";
    public final String ORB_C_PNG = "images/eternal/cardui/512/energy_orb_default_c.png";

    public final TextureCache CARD_BACKGROUND_ATTACK      = new TextureCache(ATTACK_PNG);
    public final TextureCache CARD_BACKGROUND_POWER       = new TextureCache(POWER_PNG);
    public final TextureCache CARD_BACKGROUND_SKILL       = new TextureCache(SKILL_PNG);
    public final TextureCache CARD_BACKGROUND_ATTACK_L      = new TextureCache(ATTACK_PNG_L);
    public final TextureCache CARD_BACKGROUND_POWER_L       = new TextureCache(POWER_PNG_L);
    public final TextureCache CARD_BACKGROUND_SKILL_L       = new TextureCache(SKILL_PNG_L);
}
