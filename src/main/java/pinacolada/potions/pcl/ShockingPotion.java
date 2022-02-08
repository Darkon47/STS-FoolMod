package pinacolada.potions.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.potions.PCLPotion;
import pinacolada.utilities.PCLActions;

public class ShockingPotion extends PCLPotion
{
    public static final String POTION_ID = CreateFullID(ShockingPotion.class);

    public ShockingPotion()
    {
        super(POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionEffect.NONE, Color.YELLOW, Color.GOLD, Color.GOLDENROD);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.ApplyElectrified(TargetHelper.Normal(target), potency);
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 4;
    }
}
