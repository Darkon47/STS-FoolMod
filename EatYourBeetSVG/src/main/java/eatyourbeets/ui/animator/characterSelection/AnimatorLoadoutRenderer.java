package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.loadouts._Random;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class AnimatorLoadoutRenderer extends GUIElement
{
    protected static FieldInfo<String> _hp = JavaUtilities.GetField("hp", CharacterOption.class);
    protected static FieldInfo<Integer> _gold = JavaUtilities.GetField("gold", CharacterOption.class);

    protected static final AnimatorLoadout RANDOM_LOADOUT = new _Random();
    protected static final AnimatorStrings.CharacterSelect charSelectStrings = GR.Animator.Strings.CharSelect;

    protected final ArrayList<AnimatorLoadout> loadouts;
    protected final Hitbox startingCardsLabelHb;
    protected final Hitbox startingCardsSelectedHb;
    protected final Hitbox startingCardsLeftHb;
    protected final Hitbox startingCardsRightHb;

    protected CharacterSelectScreen selectScreen;
    protected CharacterOption characterOption;
    protected String lockedDescription;
    protected AnimatorLoadout loadout;

    public AnimatorLoadoutRenderer()
    {
        float leftTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.LeftText, 9999.0F, 0.0F); // Ascension
        float rightTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.RightText, 9999.0F, 0.0F); // Level 22

        float POS_X = 180f * Settings.scale;
        float POS_Y = ((float) Settings.HEIGHT / 2.0F) + (20 * Settings.scale);

        loadouts = new ArrayList<>();
        startingCardsLabelHb = new Hitbox(leftTextWidth, 50.0F * Settings.scale);
        startingCardsSelectedHb = new Hitbox(rightTextWidth, 50f * Settings.scale);
        startingCardsLeftHb = new Hitbox(70.0F * Settings.scale, 50.0F * Settings.scale);
        startingCardsRightHb = new Hitbox(70.0F * Settings.scale, 50.0F * Settings.scale);

        startingCardsLabelHb.move(POS_X + (leftTextWidth / 2f), POS_Y);
        startingCardsLeftHb.move(startingCardsLabelHb.x + startingCardsLabelHb.width + (20 * Settings.scale), POS_Y - (10 * Settings.scale));
        startingCardsSelectedHb.move(startingCardsLeftHb.x + startingCardsLeftHb.width + (rightTextWidth / 2f), POS_Y);
        startingCardsRightHb.move(startingCardsSelectedHb.x + startingCardsSelectedHb.width + (10 * Settings.scale), POS_Y - (10 * Settings.scale));
    }

    public void Refresh(CharacterSelectScreen selectScreen, CharacterOption characterOption)
    {
        this.selectScreen = selectScreen;
        this.characterOption = characterOption;

        this.loadouts.clear();
        this.loadouts.addAll(GR.Animator.Data.BaseLoadouts);
        if (GR.Animator.Config.GetDisplayBetaSeries())
        {
            for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
            {
                if (loadout.GetStartingDeck().size() > 0)
                {
                    this.loadouts.add(loadout);
                }
            }
        }

        this.loadouts.sort((a, b) ->
        {
            int diff = a.Name.compareTo(b.Name);
            int level = GR.Animator.GetUnlockLevel();
            int levelA = a.UnlockLevel - level;
            int levelB = b.UnlockLevel - level;
            if (levelA > 0 || levelB > 0)
            {
                return diff + Integer.compare(levelA, levelB) * 1313;
            }

            return diff;
        });

        this.loadouts.add(0, RANDOM_LOADOUT);
        this.loadout = GR.Animator.Data.SelectedLoadout;
        if (RANDOM_LOADOUT != loadout && (this.loadout.GetStartingDeck().isEmpty() || !loadouts.contains(this.loadout)))
        {
            this.loadout = GR.Animator.Data.SelectedLoadout = loadouts.get(0);
        }

        _gold.Set(characterOption, loadout.StartingGold);
        _hp.Set(characterOption, String.valueOf(loadout.MaxHP));
        selectScreen.bgCharImg = GR.Animator.Images.GetCharacterPortrait(loadout.ID);

        int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel >= loadout.UnlockLevel)
        {
            lockedDescription = null;
        }
        else
        {
            lockedDescription = GR.Animator.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, currentLevel);
        }

        AnimatorCharacterSelectScreen.TrophiesRenderer.Refresh(loadout);
        AnimatorCharacterSelectScreen.SpecialTrophiesRenderer.Refresh();
    }

    public void Update()
    {
        startingCardsLabelHb.update();
        startingCardsRightHb.update();
        startingCardsLeftHb.update();

        if (InputHelper.justClickedLeft)
        {
            if (startingCardsLabelHb.hovered)
            {
                startingCardsLabelHb.clickStarted = true;
            }
            else if (startingCardsRightHb.hovered)
            {
                startingCardsRightHb.clickStarted = true;
            }
            else if (startingCardsLeftHb.hovered)
            {
                startingCardsLeftHb.clickStarted = true;
            }
        }

        if (startingCardsLeftHb.clicked)
        {
            startingCardsLeftHb.clicked = false;

            int current = loadouts.indexOf(loadout);
            if (current == 0)
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(loadouts.size() - 1);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current - 1);
            }

            Refresh(selectScreen, characterOption);
        }

        if (startingCardsRightHb.clicked)
        {
            startingCardsRightHb.clicked = false;

            int current = loadouts.indexOf(loadout);
            if (current >= (loadouts.size() - 1))
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(0);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current + 1);
            }

            Refresh(selectScreen, characterOption);
        }
    }

    public void Render(SpriteBatch sb)
    {
        Color textColor;
        String description;
        if (lockedDescription != null)
        {
            description = lockedDescription;
            textColor = Settings.RED_TEXT_COLOR;
            selectScreen.confirmButton.isDisabled = true;
        }
        else
        {
            description = loadout.GetDeckPreviewString();
            textColor = Settings.GREEN_TEXT_COLOR;
            selectScreen.confirmButton.isDisabled = false;
        }

        float originalScale = FontHelper.cardTitleFont_small.getData().scaleX;
        FontHelper.cardTitleFont_small.getData().setScale(Settings.scale * 0.8f);

        FontHelper.renderFont(sb, FontHelper.cardTitleFont_small, description, startingCardsSelectedHb.x, startingCardsSelectedHb.cY + (20 * Settings.scale), textColor);
        FontHelper.cardTitleFont_small.getData().setScale(Settings.scale * originalScale);

        FontHelper.renderFont(sb, FontHelper.cardTitleFont, charSelectStrings.LeftText, startingCardsLabelHb.x, startingCardsLabelHb.cY, Settings.GOLD_COLOR);
        FontHelper.renderFont(sb, FontHelper.cardTitleFont, loadout.Name, startingCardsSelectedHb.x, startingCardsSelectedHb.cY, Settings.CREAM_COLOR);//.BLUE_TEXT_COLOR);

        sb.setColor(startingCardsLeftHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_LEFT_ARROW, startingCardsLeftHb.cX - 24.0F, startingCardsLeftHb.cY - 24.0F, 24.0F, 24.0F,
                48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        sb.setColor(startingCardsRightHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_RIGHT_ARROW, startingCardsRightHb.cX - 24.0F, startingCardsRightHb.cY - 24.0F, 24.0F, 24.0F,
                48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
    }
}