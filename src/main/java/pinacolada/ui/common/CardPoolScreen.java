package pinacolada.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.resources.PGR;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_CardGrid;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.utilities.PCLGameUtilities;

public class CardPoolScreen extends AbstractScreen
{
    private final GUI_Button openButton;
    private final GUI_Toggle upgradeToggle;
    private final GUI_Toggle colorlessToggle;
    public GUI_CardGrid cardGrid;

    public CardPoolScreen()
    {
        cardGrid = new GUI_CardGrid()
                .ShowScrollbar(true);

        upgradeToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(PGR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.8f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
                .SetText(SingleCardViewPopup.TEXT[6])
                .SetOnToggle(CardPoolScreen::ToggleViewUpgrades);

        colorlessToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(PGR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.75f)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
                .SetText(PGR.PCL.Strings.SeriesSelectionButtons.ShowColorless)
                .SetOnToggle(val -> {
                    PGR.UI.CardFilters.ColorsDropdown.ToggleSelection(AbstractCard.CardColor.COLORLESS, val, true);
                    PGR.UI.CardFilters.ColorsDropdown.ToggleSelection(AbstractCard.CardColor.CURSE, val, true);
                });

        openButton = new GUI_Button(PGR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                .SetBorder(PGR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(PGR.PCL.Strings.Misc.Filters)
                .SetOnClick(() -> {
                    if (PGR.UI.CardFilters.isActive) {
                        PGR.UI.CardFilters.Close();
                    }
                    else {
                        PGR.UI.CardFilters.Open();
                    }
                })
                .SetColor(Color.GRAY);
    }

    public void Open(CardGroup cards)
    {
        super.Open();

        cardGrid.Clear();
        colorlessToggle.SetToggle(false);
        if (cards.isEmpty())
        {
            AbstractDungeon.closeCurrentScreen();
            return;
        }

        cardGrid.SetCardGroup(cards);
        PGR.UI.CustomHeader.setGroup(cards);
        PGR.UI.CustomHeader.SetupButtons(!PCLGameUtilities.IsPlayerClass(PGR.Fool.PlayerClass));
        PGR.UI.CardFilters.Initialize(__ -> {
            PGR.UI.CustomHeader.UpdateForFilters();
            PGR.UI.CardAffinities.Open(PGR.UI.CustomHeader.group.group);
        }, PGR.UI.CustomHeader.originalGroup, PGR.UI.CustomHeader.IsColorless());
        PGR.UI.CustomHeader.UpdateForFilters();

        if (PCLGameUtilities.InGame())
        {
            AbstractDungeon.overlayMenu.cancelButton.show(MasterDeckViewScreen.TEXT[1]);
        }

        PGR.UI.CardAffinities.SetActive(true);
        PGR.UI.CardAffinities.Open(cardGrid.cards.group);
    }

    @Override
    public void Update()
    {
        cardGrid.TryUpdate();
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
        colorlessToggle.Update();
        PGR.UI.CustomHeader.update();
        PGR.UI.CardFilters.TryUpdate();
        if (!PGR.UI.CardFilters.isActive) {
            openButton.TryUpdate();
        }

        PGR.UI.CardAffinities.TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        cardGrid.TryRender(sb);
        upgradeToggle.Render(sb);
        colorlessToggle.Render(sb);
        PGR.UI.CustomHeader.render(sb);
        PGR.UI.CardFilters.TryRender(sb);
        if (!PGR.UI.CardFilters.isActive) {
            openButton.TryRender(sb);
        }
        PGR.UI.CardAffinities.TryRender(sb);
    }

    private static void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }
}