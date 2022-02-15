package pinacolada.resources.fool;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import pinacolada.cards.base.PCLCardData;
import pinacolada.characters.FoolCharacter;
import pinacolada.resources.PCLAbstractResources;
import pinacolada.resources.PGR;
import pinacolada.ui.seriesSelection.PCLLoadoutsContainer;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class FoolResources extends PCLAbstractResources {
    public static final String ID = "fool";

    public final FoolPlayerData Data = new FoolPlayerData();
    public final FoolImages Images = new FoolImages();

    public FoolResources() {
        super(ID, Enums.Cards.THE_FOOL, Enums.Characters.THE_FOOL);
    }

    public int GetUnlockLevel()
    {
        return UnlockTracker.getUnlockLevel(PlayerClass);
    }

    public int GetUnlockCost()
    {
        return GetUnlockCost(0, true);
    }

    public int GetUnlockCost(int level, boolean relative)
    {
        if (relative)
        {
            level += GetUnlockLevel();
        }

        return level <= 4 ? 300 + (level * 500) : 1000 + (level * 300);
    }

    public boolean IsSelected()
    {
        return PCLGameUtilities.IsPlayerClass(PlayerClass);
    }

    public void InitializeColor()
    {
        Color color = CardHelper.getColor(210, 106, 147);
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
                PGR.PCL.Images.ATTACK_PNG, PGR.PCL.Images.SKILL_PNG, PGR.PCL.Images.POWER_PNG,
                PGR.PCL.Images.ORB_A_PNG, PGR.PCL.Images.ATTACK_PNG_L, PGR.PCL.Images.SKILL_PNG_L,
                PGR.PCL.Images.POWER_PNG_L, PGR.PCL.Images.ORB_B_PNG, PGR.PCL.Images.ORB_C_PNG);
    }

    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new FoolCharacter(), PGR.Fool.Images.CHAR_BUTTON_PNG, PGR.Fool.Images.CHAR_BACKGROUND, PlayerClass);
    }

    protected void InitializeStrings()
    {
        PCLJUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(CharacterStrings.class);
        LoadCustomCardStrings();
        LoadMetadata();
        LoadCustomStrings(RelicStrings.class);
    }

    protected void InitializeCards()
    {
        PCLJUtils.LogInfo(this, "InitializeCards();");

        LoadCustomCards();
        LoadCustomRelics();
        PCLCardData.PostInitialize();
    }

    protected void PostInitialize()
    {
        PCLLoadoutsContainer.PreloadResources();
        Data.Initialize();
    }

}
