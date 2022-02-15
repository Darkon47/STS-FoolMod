package pinacolada.blights.common;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.resources.PGR;
import pinacolada.resources.fool.FoolPlayerData;
import pinacolada.utilities.PCLGameUtilities;

public class GlyphBlight2 extends AbstractGlyphBlight
{
    public static final String ID = CreateFullID(GlyphBlight2.class);

    public GlyphBlight2()
    {
        super(ID, PGR.PCL.Config.AscensionGlyph2, FoolPlayerData.ASCENSION_GLYPH1_UNLOCK, FoolPlayerData.ASCENSION_GLYPH1_LEVEL_STEP, 10, 5);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            mo.increaseMaxHp(Math.max(1, mo.maxHealth * GetPotency() / 100), true);
        }
    }

}