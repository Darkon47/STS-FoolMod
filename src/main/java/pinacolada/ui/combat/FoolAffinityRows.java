package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinity;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Ftue;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class FoolAffinityRows extends GUIElement {

    protected final DraggableHitbox hb;
    protected final GUI_Image dragAmount_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Button info_icon;
    protected final ArrayList<PCLAffinityRow> rows = new ArrayList<>();

    protected final RotatingList<String> tooltipTitles = new RotatingList<>();
    protected final RotatingList<String> tooltipDescriptions = new RotatingList<>();
    protected GUI_Ftue ftue;
    protected PCLCardTooltip tooltip;
    protected Vector2 amountsSavedPosition;

    public FoolAffinityRows(PCLAffinitySystem system)
    {
        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));


        dragAmount_image = new GUI_Image(PGR.PCL.Images.Panel_Rounded.Texture(), hb)
                .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(PGR.PCL.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
                .SetColor(Colors.White(0.75f));
        info_icon = new GUI_Button(ImageMaster.INTENT_UNKNOWN, new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(100f), Scale(20f), false))
                .SetText("");
        // TODO add FTUE with dropdown and images

        final PCLAffinity[] types = PCLAffinity.Extended();
        for (int i = 0; i < types.length; i++)
        {
            rows.add(new PCLAffinityRow(system, hb, types[i], i));
        }

        tooltip = new PCLCardTooltip("", "");
        tooltip.subText = new ColoredString("", Settings.PURPLE_COLOR);

    }

    public void Initialize() {
        if (amountsSavedPosition != null) {
            final DraggableHitbox hb = (DraggableHitbox) dragAmount_image.hb;
            amountsSavedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            amountsSavedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (amountsSavedPosition.dst2(PGR.PCL.Config.AffinitySystemPosition.Get()) > Mathf.Epsilon) {
                PCLJUtils.LogInfo(this, "Saved PCL affinity panel position.");
                PGR.PCL.Config.AffinitySystemPosition.Set(amountsSavedPosition.cpy(), true);
            }
        }

        for (PCLAffinityRow row : rows) {
            row.Initialize();
        }

        if (tooltipDescriptions.Count() == 0 || tooltipTitles.Count() == 0) {
            for (String tip : PGR.PCL.Strings.Tutorial.TutorialItems()) {
                tooltipDescriptions.Add(tip);
            }
            tooltipTitles.Add(PGR.Tooltips.Affinity_General.title);
            tooltipTitles.Add(PGR.Tooltips.Match.title);
            tooltipTitles.Add(PGR.Tooltips.Match.title);

            SetTipIndex(0);
        }
    }

    public PCLAffinityRow GetRow(PCLAffinity affinity) {
        return affinity.ID < rows.size() && affinity.ID >= 0 ? rows.get(affinity.ID) : null;
    }

    public void CycleTips() {
        tooltipTitles.Next(true);
        UpdateTipContent();
    }

    public void SetTipIndex(int index) {
        tooltipTitles.SetIndex(index);
        UpdateTipContent();
    }

    protected void UpdateTipContent() {
        tooltipDescriptions.SetIndex(tooltipTitles.GetIndex());
        tooltip.description = tooltipDescriptions.Current();
        tooltip.title = tooltipTitles.Current();
        tooltip.subText.SetText(PGR.PCL.Strings.Misc.PressKeyToCycle(PCLHotkeys.cycle.getKeyString()) + " (" + (tooltipDescriptions.GetIndex() + 1) + "/" + tooltipDescriptions.Count() + ")");
    }

    public void Flash(PCLAffinity affinity) {
        PCLAffinityRow row = GetRow(affinity);
        if (row != null) {
            row.Flash();
        }
    }

    public boolean CanActivateSynergyBonus(PCLCardAffinity affinity) {
        return affinity != null && affinity.level > 0 && CanActivateSynergyBonus(affinity.type);
    }

    public boolean CanActivateSynergyBonus(PCLAffinity affinity) {
        return affinity.ID >= 0 && GetRow(affinity).Power.IsEnabled();
    }

    public void OnStartOfTurn() {
        for (PCLAffinityRow row : rows) {
            row.OnStartOfTurn();
        }
    }

    public void OnMatch(PCLCard card) {
        int star = card.affinities.Star != null ? card.affinities.Star.level : 0;
        if (star > 0) {
            for (PCLAffinityRow row : rows) {
                if (CanActivateSynergyBonus(row.Type)) {
                    row.ActivateSynergyBonus(card);
                }
            }
        } else {
            for (PCLCardAffinity affinity : card.affinities.GetCardAffinities(false)) {
                if (CanActivateSynergyBonus(affinity)) {
                    final PCLAffinityRow row = GetRow(affinity.type);
                    if (row != null) {
                        row.ActivateSynergyBonus(card);
                    }
                }
            }
        }
    }

    public void Update() {
        Update(null);
    }

    public void Update(PCLCard card)
    {
        if (amountsSavedPosition == null)
        {
            amountsSavedPosition = PGR.PCL.Config.AffinitySystemPosition.Get(new Vector2(0.0522f, 0.43f)).cpy();
            hb.SetPosition(ScreenW(amountsSavedPosition.x), ScreenH(amountsSavedPosition.y));
        }

        boolean draggingCard = false;
        PCLCard hoveredCard = null;
        if (player.hoveredCard != null)
        {
            if ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode)
            {
                draggingCard = true;
            }
            if (player.hoveredCard instanceof PCLCard)
            {
                hoveredCard = (PCLCard)player.hoveredCard;
            }
        }

        final PCLAffinityCounts previewAffinities = new PCLAffinityCounts(PCLCombatStats.MatchingSystem.AffinityCounts);
        //final EYBCardAffinities synergies = GetSynergies(hoveredCard, lastCardPlayed);
        for (PCLAffinityRow row : rows)
        {
            row.Update(previewAffinities, hoveredCard, null, draggingCard);
        }

        dragAmount_image.Update();
        draggable_icon.Update();
        info_icon.Update();

        boolean isHovered = dragAmount_image.hb.hovered || info_icon.hb.hovered || PCLJUtils.Any(rows, PCLAffinityRow::IsHovered);
        dragAmount_image.SetColor(0.05f, 0.05f, 0.05f, isHovered ? 0.5f : 0.05f);
        draggable_icon.SetColor(Colors.White(isHovered ? 0.75f : 0.1f));

        if (!draggingCard && info_icon.hb.hovered)
        {
            PCLCardTooltip.QueueTooltip(tooltip);
            if (PCLHotkeys.cycle.isJustPressed()) {
                CycleTips();
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        dragAmount_image.Render(sb);
        draggable_icon.Render(sb);
        info_icon.Render(sb);

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, PGR.PCL.Strings.Combat.Experience, info_icon.hb.cX + Scale(92), info_icon.hb.y, 1f, Colors.Blue(1f));
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, PGR.PCL.Strings.Combat.Uses, info_icon.hb.cX + Scale(156), info_icon.hb.y, 1f, Colors.Blue(1f));

        for (PCLAffinityRow t : rows)
        {
            t.Render(sb);
        }
    }

}
