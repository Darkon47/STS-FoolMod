package pinacolada.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.ui.GUIElement;
import pinacolada.resources.PGR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.utilities.PCLGameUtilities;

public abstract class AbstractScreen extends GUIElement
{
    public static final AbstractDungeon.CurrentScreen ScreenType = PGR.Enums.Screens.EYB_SCREEN;

    public boolean CanOpen()
    {
        return IsNullOrNone(AbstractDungeon.previousScreen) && !CardCrawlGame.isPopupOpen;
    }

    protected void Open()
    {
        PGR.UI.CurrentScreen = this;
        Settings.hideTopBar = true;
        Settings.hideRelics = true;

        AbstractDungeon.previousScreen = AbstractDungeon.screen;
        AbstractDungeon.screen = ScreenType;
        AbstractDungeon.isScreenUp = true;

        if (PCLGameUtilities.InBattle())
        {
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.hideCombatPanels();
        }

        if (PCLGameUtilities.InGame())
        {
            AbstractDungeon.topPanel.unhoverHitboxes();
            AbstractDungeon.topPanel.potionUi.isHidden = true;

            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.overlayMenu.showBlackScreen(0.7f);
        }

        CardCrawlGame.sound.play("DECK_OPEN");
    }

    public void Dispose()
    {
        // Modified Logic from AbstractDungeon.closeCurrentScreen and AbstractDungeon.genericScreenOverlayReset
        PGR.UI.CurrentScreen = null;
        Settings.hideTopBar = false;
        Settings.hideRelics = false;

        AbstractDungeon.CurrentScreen previous = AbstractDungeon.previousScreen;
        if (previous == AbstractDungeon.CurrentScreen.NONE)
        {
            AbstractDungeon.previousScreen = null;
            AbstractDungeon.screen = previous;
        }

        if (AbstractDungeon.player == null || !PCLGameUtilities.InGame())
        {
            AbstractDungeon.isScreenUp = !IsNullOrNone(previous);
            return;
        }

        if (IsNullOrNone(previous) || previous == ScreenType)
        {
            if (AbstractDungeon.player.isDead)
            {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
            }
            else
            {
                AbstractDungeon.isScreenUp = false;
                AbstractDungeon.overlayMenu.hideBlackScreen();
            }
        }

        AbstractDungeon.overlayMenu.cancelButton.hide();

        if (PCLGameUtilities.InBattle(true))
        {
            AbstractDungeon.overlayMenu.showCombatPanels();
        }
    }

    public void Update()
    {

    }

    public void Render(SpriteBatch sb)
    {

    }

    public static GUI_Button CreateHexagonalButton(float x, float y, float width, float height)
    {
        final Texture buttonTexture = PGR.PCL.Images.HexagonalButton.Texture();
        final Texture buttonBorderTexture = PGR.PCL.Images.HexagonalButtonBorder.Texture();
        return new GUI_Button(buttonTexture, x, y)
        .SetBorder(buttonBorderTexture, Color.WHITE)
        .SetClickDelay(0.3f)
        .SetDimensions(width, height);
    }

    private static boolean IsNullOrNone(AbstractDungeon.CurrentScreen screen)
    {
        return screen == null || screen == AbstractDungeon.CurrentScreen.NONE;
    }
}
