package pinacolada.potions.fool;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.potions.PCLPotion;
import pinacolada.utilities.PCLActions;

public class ColorfulPotion extends PCLPotion
{
    public static final String POTION_ID = CreateFullID(ColorfulPotion.class);

    public ColorfulPotion()
    {
        super(POTION_ID, PotionRarity.COMMON, PotionSize.JAR, PotionEffect.NONE, Color.PURPLE, Color.MAGENTA, Color.GOLDENROD);
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.TryChooseGainAffinity(name, potency, PCLAffinity.Basic());
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 4;
    }
}
