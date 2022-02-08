package pinacolada.potions.pcl;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.potions.PCLPotion;
import pinacolada.utilities.PCLActions;

public class ChillingPotion extends PCLPotion
{
    public static final String POTION_ID = CreateFullID(ChillingPotion.class);

    public ChillingPotion()
    {
        super(POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionEffect.NONE, Color.SKY, Color.NAVY, Color.BLUE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(target), potency);
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 4;
    }
}
