package pinacolada.relics.fool.replacement;

import pinacolada.orbs.pcl.Fire;
import pinacolada.relics.FoolReplacementRelic;
import pinacolada.utilities.PCLActions;

public class AlchemistGlove extends FoolReplacementRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class);

    public AlchemistGlove()
    {
        super(ID, eatyourbeets.relics.animator.AlchemistGlove.ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLActions.Bottom.ChannelOrb(new Fire());
    }
}