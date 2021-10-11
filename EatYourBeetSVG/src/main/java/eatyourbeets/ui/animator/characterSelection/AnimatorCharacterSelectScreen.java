package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameUtilities;
import patches.unlockTracker.UnlockTrackerPatches;

import java.awt.*;
import java.net.URI;

public class AnimatorCharacterSelectScreen
{
    protected static final AnimatorLoadoutRenderer LoadoutRenderer = new AnimatorLoadoutRenderer();
    protected static final AnimatorTrophiesRenderer TrophiesRenderer = new AnimatorTrophiesRenderer();
    protected static final AnimatorSpecialTrophiesRenderer SpecialTrophiesRenderer = new AnimatorSpecialTrophiesRenderer();
    protected static CharacterOption selectedOption;
    protected static GUI_Button DiscordButton;
    protected static GUI_Button SteamButton;
    protected static GUI_Label UnofficialDisclaimer;

    public static void Initialize(CharacterSelectScreen selectScreen)
    {
        GameUtilities.UnlockAllKeys();
        selectedOption = null;

        final float size = Settings.scale * 36;

        DiscordButton = new GUI_Button(GR.Common.Images.Discord.Texture(), new AdvancedHitbox(0, 0, size, size))
        .SetPosition(Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.95f).SetText("")
        .SetOnClick(() -> Browse(DiscordButton));

        SteamButton = new GUI_Button(GR.Common.Images.Steam.Texture(), new AdvancedHitbox(0, 0, size, size))
        .SetPosition(Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.95f - DiscordButton.hb.height * 1.1f).SetText("")
        .SetOnClick(() -> Browse(SteamButton));

        DiscordButton.SetActive(SteamButton.SetActive(Desktop.isDesktopSupported()).isActive);

        UnofficialDisclaimer = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
                new AdvancedHitbox(0, 0, Settings.WIDTH * 0.55f, size * 2f))
                .SetPosition(Settings.WIDTH * 0.29f, Settings.HEIGHT * 0.93f)
                .SetAlignment(0.95f, 0.05f, true) // 0.1f
                .SetText(GR.Animator.Strings.Misc.UnofficialDisclaimer);

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

    private static void UpdateSelectedCharacter(CharacterSelectScreen selectScreen)
    {
        final CharacterOption current = selectedOption;

        selectedOption = null;

        for (CharacterOption o : selectScreen.options)
        {
            if (o.selected)
            {
                if (o.c.chosenClass == GR.Enums.Characters.THE_ANIMATOR)
                {
                    selectedOption = o;

                    if (current != o)
                    {
                        LoadoutRenderer.Refresh(selectScreen, o);
                    }
                }
                else if (o.c.chosenClass == GR.Enums.Characters.THE_UNNAMED)
                {
                    selectScreen.confirmButton.isDisabled = true;
                }

                return;
            }
        }
    }

    public static void RenderOption(CharacterOption instance, SpriteBatch sb)
    {
        LoadoutRenderer.Render(sb);
        TrophiesRenderer.Render(sb);
        SpecialTrophiesRenderer.Render(sb);
        DiscordButton.TryRender(sb);
        SteamButton.TryRender(sb);
        UnofficialDisclaimer.TryRender(sb);
    }

    public static void UpdateOption(CharacterOption instance)
    {
        LoadoutRenderer.Update();
        TrophiesRenderer.Update();
        SpecialTrophiesRenderer.Update();
        DiscordButton.TryUpdate();
        SteamButton.TryUpdate();
        UnofficialDisclaimer.TryUpdate();

        final float offsetX = 60 * Settings.scale;
        final float offsetY = 0;
        final AnimatorStrings.Misc misc = GR.Animator.Strings.Misc;

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

}
