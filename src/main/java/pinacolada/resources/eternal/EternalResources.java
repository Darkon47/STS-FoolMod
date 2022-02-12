package pinacolada.resources.eternal;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import pinacolada.cards.base.PCLCardData;
import pinacolada.characters.EternalCharacter;
import pinacolada.resources.PCLAbstractResources;
import pinacolada.resources.PGR;
import pinacolada.ui.seriesSelection.PCLLoadoutsContainer;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class EternalResources extends PCLAbstractResources {
    public static final String ID = "eternal";

    public final AbstractCard.CardColor CardColor = Enums.Cards.THE_ETERNAL;
    public final AbstractPlayer.PlayerClass PlayerClass = Enums.Characters.THE_ETERNAL;
    public final EternalImages Images = new EternalImages();

    public EternalResources() {
        super(ID);
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
        Color color = CardHelper.getColor(157, 76, 210);
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
                PGR.Eternal.Images.ATTACK_PNG, PGR.Eternal.Images.SKILL_PNG, PGR.Eternal.Images.POWER_PNG,
                PGR.Eternal.Images.ORB_A_PNG, PGR.Eternal.Images.ATTACK_PNG_L, PGR.Eternal.Images.SKILL_PNG_L,
                PGR.Eternal.Images.POWER_PNG_L, PGR.Eternal.Images.ORB_B_PNG, PGR.Eternal.Images.ORB_C_PNG);
    }

    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new EternalCharacter(), PGR.Eternal.Images.CHAR_BUTTON_PNG, PGR.Eternal.Images.CHAR_BACKGROUND, PlayerClass);
    }

    protected void InitializeStrings()
    {
        PCLJUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(CharacterStrings.class);
        LoadCustomCardStrings();
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
    }

}
