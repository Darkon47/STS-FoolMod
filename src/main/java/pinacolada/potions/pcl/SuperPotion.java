package pinacolada.potions.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.potions.PCLPotion;
import pinacolada.utilities.PCLActions;

public class SuperPotion extends PCLPotion
{
    public static final String POTION_ID = CreateFullID(SuperPotion.class);

    public SuperPotion()
    {
        super(POTION_ID, PotionRarity.RARE, PotionSize.BOLT, PotionEffect.NONE, Color.SCARLET, Color.LIME, Color.SKY);
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.GainMight(potency);
        PCLActions.Bottom.GainVelocity(potency);
        PCLActions.Bottom.GainWisdom(potency);
        PCLActions.Bottom.GainEndurance(potency);
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 5;
    }
}
