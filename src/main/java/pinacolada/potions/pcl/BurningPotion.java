package pinacolada.potions.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.potions.PCLPotion;
import pinacolada.utilities.PCLActions;

public class BurningPotion extends PCLPotion
{
    public static final String POTION_ID = CreateFullID(BurningPotion.class);

    public BurningPotion()
    {
        super(POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionEffect.NONE, Color.RED, Color.SCARLET, Color.FIREBRICK);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(target), potency);
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 4;
    }
}
