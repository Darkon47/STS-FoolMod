package pinacolada.blights.common;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import pinacolada.resources.PGR;
import pinacolada.resources.fool.FoolPlayerData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GlyphBlight1 extends AbstractGlyphBlight
{
    public static final String ID = CreateFullID(GlyphBlight1.class);

    public GlyphBlight1()
    {
        super(ID, PGR.PCL.Config.AscensionGlyph1, FoolPlayerData.ASCENSION_GLYPH1_UNLOCK, FoolPlayerData.ASCENSION_GLYPH1_LEVEL_STEP, 0, 1);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        int potency = GetPotency();
        if (potency > 0) {
            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                PCLActions.Top.StackPower(mo, new RitualPower(mo, potency, false));
            }
        }
    }

}