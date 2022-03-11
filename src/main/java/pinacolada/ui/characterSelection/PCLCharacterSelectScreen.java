package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.patches.unlockTracker.UnlockTrackerPatches;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLGameUtilities;

import java.awt.*;
import java.net.URI;

public class PCLCharacterSelectScreen
{
    protected static final PCLCharacterSelectOptionsRenderer LoadoutRenderer = new PCLCharacterSelectOptionsRenderer();
    protected static CharacterOption selectedOption;
    protected static GUI_Button DiscordButton;
    protected static GUI_Button SteamButton;
    protected static GUI_Label CustomDisclaimer;

    public static void Initialize(CharacterSelectScreen selectScreen)
    {
        PCLGameUtilities.UnlockAllKeys();
        selectedOption = null;

        final float size = Settings.scale * 36;

        DiscordButton = new GUI_Button(PGR.PCL.Images.Discord.Texture(), new AdvancedHitbox(0, 0, size, size))
        .SetPosition(Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.95f).SetText("")
        .SetOnClick(() -> Browse(DiscordButton));

        SteamButton = new GUI_Button(PGR.PCL.Images.Steam.Texture(), new AdvancedHitbox(0, 0, size, size))
        .SetPosition(Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.95f - DiscordButton.hb.height * 1.1f).SetText("")
        .SetOnClick(() -> Browse(SteamButton));

        DiscordButton.SetActive(SteamButton.SetActive(Desktop.isDesktopSupported()).isActive);

        CustomDisclaimer = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
                new AdvancedHitbox(0, 0, Settings.WIDTH * 0.55f, size * 2f))
                .SetPosition(Settings.WIDTH * 0.29f, Settings.HEIGHT * 0.93f)
                .SetAlignment(0.95f, 0.05f, true) // 0.1f
                .SetText(PGR.PCL.Strings.Misc.CustomDisclaimer);

        UnlockTrackerPatches.Validate();
    }

    public static void Update(CharacterSelectScreen selectScreen)
    {
        UpdateSelectedCharacter(selectScreen);
    }

    public static void Render(CharacterSelectScreen selectScreen, SpriteBatch sb)
    {
        // RenderOption is being called instead
    }

    public static void RandomizeLoadout()
    {
        LoadoutRenderer.RandomizeLoadout();
    }

    public static void UpdateForAscensionChange(CharacterSelectScreen selectScreen) {
        for (PCLGlyphEditor glyphEditor : LoadoutRenderer.glyphEditors) {
            glyphEditor.Refresh(selectScreen.ascensionLevel);
        }
    }

    private static void UpdateSelectedCharacter(CharacterSelectScreen selectScreen)
    {
        final CharacterOption current = selectedOption;

        selectedOption = null;

        for (CharacterOption o : selectScreen.options)
        {
            if (o.selected)
            {
                if (PCLGameUtilities.IsPCLPlayerClass(o.c.chosenClass))
                {
                    selectedOption = o;

                    if (current != o && selectedOption.c.chosenClass == PGR.Enums.Characters.THE_FOOL)
                    {
                        LoadoutRenderer.Refresh(selectScreen, o);
                    }
                }

                return;
            }
        }
    }

    public static void RenderOption(CharacterOption instance, SpriteBatch sb)
    {
        AbstractPlayer.PlayerClass pc = GetCurrentClass(instance);
        if (pc == PGR.Enums.Characters.THE_FOOL) {
            LoadoutRenderer.Render(sb);
        }
        DiscordButton.TryRender(sb);
        SteamButton.TryRender(sb);
        if (PGR.PCL.Config.CustomCardsEnabledCharacters.Get().contains(pc)) {
            CustomDisclaimer.TryRender(sb);
        }
    }

    public static void UpdateOption(CharacterOption instance)
    {
        AbstractPlayer.PlayerClass pc = GetCurrentClass(instance);
        if (pc == PGR.Enums.Characters.THE_FOOL) {
            LoadoutRenderer.Update();
        }
        DiscordButton.TryUpdate();
        SteamButton.TryUpdate();
        CustomDisclaimer.TryUpdate();

        final float offsetX = 60 * Settings.scale;
        final float offsetY = 0;
        final PCLStrings.Misc misc = PGR.PCL.Strings.Misc;

        if (DiscordButton.interactable && DiscordButton.hb.hovered)
        {
            TipHelper.renderGenericTip(DiscordButton.hb.cX + offsetX, DiscordButton.hb.cY + offsetY, misc.Discord, misc.DiscordDescription);
        }
        else if (SteamButton.interactable && SteamButton.hb.hovered)
        {
            TipHelper.renderGenericTip(SteamButton.hb.cX + offsetX, SteamButton.hb.cY + offsetY, misc.Steam, misc.SteamDescription);
        }
    }

    private static void Browse(GUI_Button button)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                URI uri = null;
                if (button == SteamButton)
                {
                    uri = new URI("https://steamcommunity.com/sharedfiles/filedetails/?id=1638308801");
                }
                else if (button == DiscordButton)
                {
                    uri = new URI("https://discord.gg/SmHMmJR");
                }

                if (uri != null)
                {
                    Desktop.getDesktop().browse(uri);
                    button.SetInteractable(false);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static AbstractPlayer.PlayerClass GetCurrentClass(CharacterOption instance) {
        return selectedOption != null && selectedOption.c != null ? selectedOption.c.chosenClass : null;
    }

}
