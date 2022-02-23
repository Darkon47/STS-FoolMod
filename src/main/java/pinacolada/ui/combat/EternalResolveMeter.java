package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.utilities.*;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.SFX;
import pinacolada.effects.affinity.ChangeAffinityCountEffect;
import pinacolada.interfaces.subscribers.OnTrySpendEnergySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.special.RerollAffinityEternalPower;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.ui.common.AffinityKeywordButton;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.*;

import java.util.ArrayList;

public class EternalResolveMeter extends PCLAffinityMeter implements OnTrySpendEnergySubscriber {
    public static final int DEFAULT_REROLLS = 1;
    public static final int BASE_PREVIEWS = 4;
    public static final int BASE_RESOLVE_GAIN = 2;
    public static final int BASE_RESOLVE_COST = 2;
    public static final int MAX_PREVIEWS = 8;
    public static final int MAX_RESOLVE = 20;
    public static final float ROTATION_SPEED_NORMAL = 20f;
    public static final float ROTATION_SPEED_ULTIMATE = 160f;
    public static final float ROTATION_CHANGE_DURATION = 1.5f;
    public static final float ICON_SIZE = Scale(48);
    private static final WeightedList<PCLAffinity> PICKS = new WeightedList<>();
    public static final Color BEGIN_COLOR = new Color(0.55f, 0.4f, 0.8f, 1f);
    public static final Color END_COLOR = new Color(0.55f, 0.8f, 0.4f, 1f);

    public final ArrayList<AffinityKeywordButton> Next = new ArrayList<>();
    public final ArrayList<GUI_Image> progressBarTicks = new ArrayList<>();
    public final ArrayList<GUI_Image> progressBarTickOverlays = new ArrayList<>();
    protected final RotatingList<String> tooltipTitles = new RotatingList<>();
    protected final RotatingList<String> tooltipDescriptions = new RotatingList<>();
    protected final GUI_Image draggable_panel;
    protected final GUI_Image draggable_icon;
    protected final GUI_Image progressBar;
    protected final GUI_Image UltimateButtonBG;
    protected final GUI_Button UltimateButton;
    protected final GUI_Button info_icon;
    protected float rotationSpeed;
    protected float rotationTimer;
    protected boolean inUltimateMode;
    protected int Resolve = 0;
    public int ResolveGain = BASE_RESOLVE_GAIN;
    public int ResolveCost = BASE_RESOLVE_COST;
    protected Vector2 meterSavedPosition;
    protected PCLCardTooltip tooltipBar;
    protected PCLCardTooltip tooltipInfo;
    private final DraggableHitbox hb;
    public RerollAffinityEternalPower Reroll;

    static {
        PICKS.Add(PCLAffinity.Light, 4);
        PICKS.Add(PCLAffinity.Dark, 4);
        PICKS.Add(PCLAffinity.Silver, 2);
    }

    public EternalResolveMeter() {
        final Texture pbTexture = PGR.PCL.Images.ProgressBar.Texture();
        final Texture pbTextureTick = PGR.PCL.Images.ProgressBarPart.Texture();
        final Texture ubBG = PGR.PCL.Images.Affinities.BorderBG4.Texture();

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), ICON_SIZE,  ICON_SIZE, true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));
        draggable_panel = new GUI_Image(PGR.PCL.Images.Panel_Rounded.Texture(), hb)
                .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(PGR.PCL.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), ICON_SIZE / 2, ICON_SIZE / 2, false))
                .SetColor(Colors.White(0.25f));
        info_icon = new GUI_Button(ImageMaster.INTENT_UNKNOWN, new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(100f), Scale(20f), false))
                .SetText("");

        this.progressBar = new GUI_Image(pbTexture,
                new RelativeHitbox(hb, pbTexture.getWidth(), pbTexture.getHeight(), hb.width + ICON_SIZE * 5, 0, false))
                .SetColor(Color.LIGHT_GRAY);
        this.UltimateButtonBG = new GUI_Image(ubBG,
                new RelativeHitbox(hb, ubBG.getWidth(), ubBG.getHeight(), hb.width + ICON_SIZE * 9f, 0, false))
                .SetColor(Color.LIGHT_GRAY);
        this.UltimateButton = new GUI_Button(PGR.PCL.Images.Effects.ElectroSigil.Texture(),
                new RelativeHitbox(hb, ICON_SIZE * 2, ICON_SIZE * 2, hb.width + ICON_SIZE * 9f, 0, false))
                .SetOnClick(() -> {
                    PCLActions.Bottom.Callback(() -> {
                       if (Resolve == MAX_RESOLVE && !InUltimateMode()) {
                           EnterUltimateMode();
                       }
                    });
                })
                .SetText(null);

        final float w = pbTextureTick.getWidth() - 3;
        for (int i = 0; i < MAX_RESOLVE; i++) {
            progressBarTicks.add(new GUI_Image(pbTextureTick,
                    new RelativeHitbox(hb, pbTextureTick.getWidth(), pbTextureTick.getHeight(), hb.width + ICON_SIZE + w * (i + 3), 0, false)
            ).SetColor(Color.DARK_GRAY)
            );
        }
        for (int i = 0; i < MAX_RESOLVE; i++) {
            progressBarTickOverlays.add(new GUI_Image(pbTextureTick,
                    new RelativeHitbox(hb, pbTextureTick.getWidth(), pbTextureTick.getHeight(), hb.width + ICON_SIZE + w * (i + 3), 0, false)
            ).SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                    .SetColor(Colors.Black(0f))
            );
        }

        tooltipBar = new PCLCardTooltip(PGR.PCL.Strings.Combat.ResolveMeter, PGR.PCL.Strings.Combat.ResolveMeterDescription1);
        tooltipInfo = new PCLCardTooltip("", "");
        tooltipInfo.subText = new ColoredString("", Settings.PURPLE_COLOR);

    }

    public void Initialize() {
        Resolve = 0;
        ResolveGain = BASE_RESOLVE_GAIN;
        ResolveCost = BASE_RESOLVE_COST;
        inUltimateMode = false;
        Next.clear();
        for (int i = 0; i < BASE_PREVIEWS; i++) {
            AddQueueItem(i);
        }

        if (meterSavedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) draggable_panel.hb;
            meterSavedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            meterSavedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (meterSavedPosition.dst2(PGR.PCL.Config.ResolveMeterPosition.Get()) > Mathf.Epsilon)
            {
                PCLJUtils.LogInfo(this, "Saved Resolve Meter position.");
                PGR.PCL.Config.ResolveMeterPosition.Set(meterSavedPosition.cpy(), true);
            }
        }

        if (tooltipDescriptions.Count() == 0 || tooltipTitles.Count() == 0) {
            for (String tip : PGR.PCL.Strings.EternalTutorial.TutorialItems()) {
                tooltipDescriptions.Add(tip);
            }
            tooltipTitles.Add(PGR.PCL.Strings.Combat.ResolveMeter);
            tooltipTitles.Add(PGR.PCL.Strings.Combat.ResolveMeter);
            tooltipTitles.Add(PGR.PCL.Strings.Combat.ResolveMeter);

            SetTipIndex(0);
        }

        Reroll = new RerollAffinityEternalPower(DEFAULT_REROLLS);

        PCLCombatStats.onTrySpendEnergy.Subscribe(this);
    }

    @Override
    public void Update(PCLCard card) {
        if (meterSavedPosition == null)
        {
            meterSavedPosition = PGR.PCL.Config.AffinityMeterPosition.Get(new Vector2(0.40f, 0.7f)).cpy();
            hb.SetPosition(ScreenW(meterSavedPosition.x), ScreenH(meterSavedPosition.y));
        }
        hb.update();

        draggable_panel.TryUpdate();
        draggable_icon.TryUpdate();
        info_icon.TryUpdate();

        boolean isHovered = draggable_panel.hb.hovered || UltimateButton.hb.hovered || progressBar.hb.hovered || info_icon.hb.hovered ;
        draggable_panel.SetColor(0.05f, 0.05f, 0.05f, isHovered ? 0.5f : 0.05f);
        draggable_icon.SetColor(Colors.White(isHovered ? 0.75f : 0.1f));

        if (info_icon.hb.hovered)
        {
            PCLCardTooltip.QueueTooltip(tooltipInfo);
            if (PCLHotkeys.cycle.isJustPressed()) {
                CycleTips();
            }
        }

        int PreviewResolve = Resolve;
        if (card != null) {
            PreviewResolve = HasMatch(card) ? Resolve + PCLCombatStats.OnTryGainResolve(card, AbstractDungeon.player, ResolveGain, false, true) :
            PCLGameUtilities.IsMismatch(card, GetCurrentAffinity()) ? Resolve + PCLCombatStats.OnTryGainResolve(card, AbstractDungeon.player, -ResolveGain, false, true) :
                            Resolve;
        }

        for (int i = 0; i < MAX_RESOLVE; i++) {
            progressBarTicks.get(i)
                    .SetTargetColor(Resolve > i ? Colors.Lerp(BEGIN_COLOR, END_COLOR, (float) i / MAX_RESOLVE) : Color.DARK_GRAY.cpy())
                    .TryUpdate();
            progressBarTickOverlays.get(i)
                    .SetTargetColor(
                                            PreviewResolve < Resolve && i >= PreviewResolve && i < Resolve ? Color.RED.cpy() :
                                            PreviewResolve > Resolve && i < PreviewResolve && i >= Resolve ? Color.GREEN.cpy() :
                                            Resolve > i && InUltimateMode() ? Colors.Lerp(BEGIN_COLOR, END_COLOR, (float) i / MAX_RESOLVE) :
                                            Colors.Black(0f))
                    .TryUpdate();
        }

        for (AffinityKeywordButton kb : Next) {
            kb.TryUpdate();
        }
        progressBar.TryUpdate();

        UltimateButtonBG.rotation = PGR.UI.Time_Multi(8);
        UltimateButton.background.rotation = PGR.UI.Time_Multi(8);
        UltimateButtonBG.SetColor(Resolve < MAX_RESOLVE && !InUltimateMode() ? Color.DARK_GRAY : Color.WHITE).TryUpdate();
        UltimateButton.SetInteractable(Resolve == MAX_RESOLVE).SetColor(Resolve < MAX_RESOLVE && !InUltimateMode() ? Color.DARK_GRAY : Color.WHITE).TryUpdate();

        if (progressBar.hb.hovered || UltimateButtonBG.hb.hovered) {
            tooltipBar.description = PGR.PCL.Strings.Combat.ResolveMeterDescriptionFull(Resolve, MAX_RESOLVE, ResolveGain, InUltimateMode());
            PCLCardTooltip.QueueTooltip(tooltipBar);
        }

        if (Reroll != null)
        {
            Reroll.updateDescription();
            if (PCLJUtils.Any(Next, a -> a.background_button.hb.hovered)) {
                PCLCardTooltip.QueueTooltip(Reroll.tooltip);
            }

            if (PCLHotkeys.rerollCurrent.isJustPressed()) {
                Reroll.OnClick();
            }
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        draggable_panel.Render(sb);
        draggable_icon.Render(sb);

        for (GUI_Image im : progressBarTicks) {
            im.TryRender(sb);
        }
        for (GUI_Image im : progressBarTickOverlays) {
            im.TryRender(sb);
        }
        progressBar.TryRender(sb);

        for (AffinityKeywordButton kb : Next) {
            kb.TryRender(sb);
        }
        UltimateButtonBG.TryRender(sb);
        UltimateButton.TryRender(sb);
        if (InUltimateMode()) {
            PCLRenderHelpers.DrawBlended(sb, PCLRenderHelpers.BlendingMode.Glowing,
                    () -> UltimateButton.Render(sb));
        }
    }

    @Override
    public boolean HasMatch(AbstractCard card) {
        PCLCard eCard = PCLJUtils.SafeCast(card, PCLCard.class);
        if (eCard != null) {
            if (GetCurrentAffinity().equals(PCLAffinity.Star)) {
                return (eCard.affinities.GetLevel(PCLAffinity.General, true) > 0);
            }
            return (eCard.affinities.GetLevel(GetCurrentAffinity(), true) > 0);
        }
        return false;
    }

    @Override
    public PCLAffinity OnMatch(AbstractCard card) {
        PCLActions.Bottom.Callback(() -> {
            AddResolve(PCLCombatStats.OnTryGainResolve(card, AbstractDungeon.player, ResolveGain, true, true) - (InUltimateMode() ? ResolveCost : 0));
        });

        return AdvanceAffinities(1);
    }

    @Override
    public PCLAffinity OnNotMatch(AbstractCard card) {
        if (PCLGameUtilities.IsMismatch(card, GetCurrentAffinity())) {
            PCLActions.Bottom.Callback(() -> {
                AddResolve(PCLCombatStats.OnTryGainResolve(card, AbstractDungeon.player, -ResolveGain, true, true) - (InUltimateMode() ? ResolveCost : 0));
            });
        }

        return AdvanceAffinities(1);
    }

    @Override
    public void OnStartOfTurn() {
        if (InUltimateMode()) {
            ExitUltimateMode();
        }
        if (Reroll != null) {
            Reroll.atStartOfTurn();
        }
    }

    @Override
    public PCLAffinity Get(int target) {
        if (target >= Next.size()) {
            return PCLAffinity.General;
        }
        return Next.get(target).Type;
    }

    @Override
    public PCLAffinity Set(PCLAffinity affinity, int target) {
        if (target >= Next.size()) {
            return PCLAffinity.General;
        }
        Next.get(target).SetAffinity(affinity);
        return affinity;
    }

    public void Flash() {
        if (Next.size() > 0) {
            PCLGameEffects.List.Add(new ChangeAffinityCountEffect(Next.get(0), true).SetScale(Settings.scale * 0.5f));
        }

    }

    public void EnterUltimateMode() {
        PCLActions.Bottom.Callback(() -> {
            if (PCLCombatStats.OnTryChangeUltimateState(AbstractDungeon.player, this, true)) {
                PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.85F);
                PCLActions.Bottom.VFX(new BorderFlashEffect(Color.PURPLE));
                SFX.Play(SFX.ATTACK_SCYTHE, 0.75f, 0.75f);
                SFX.Play(SFX.ATTACK_SCYTHE, 0.55f, 0.55f, 0.75f);
                PCLGameEffects.Queue.Add(new AdditiveSlashImpactEffect(AbstractDungeon.player.hb.cX - 100f * Settings.scale, AbstractDungeon.player.hb.cY + 100f * Settings.scale, Color.PURPLE.cpy()));
                PCLGameEffects.Queue.Add(new AnimatedSlashEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY + 20f * Settings.scale,
                        -500f, 0f, 260f, 8f, Color.PURPLE.cpy(), Color.VIOLET.cpy()));
                float wait = PCLGameEffects.Queue.Add(new AnimatedSlashEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY + 20f * Settings.scale,
                        500f, 0f, 80f, 8f, Color.PURPLE.cpy(), Color.VIOLET.cpy())).duration * 6f;
                for (int i = 0; i < 4; i++) {
                    PCLActions.Top.Wait(0.2f);
                    PCLGameEffects.Queue.Add(new AnimatedSlashEffect(AbstractDungeon.player.hb.cX - i * 10f * Settings.scale, AbstractDungeon.player.hb.cY + 20f * Settings.scale,
                            500f, 0f, 80f, 8f, Color.PURPLE.cpy(), Color.VIOLET.cpy()));
                }
                SFX.Play(SFX.GHOST_FLAMES);
                inUltimateMode = true;
                PCLGameUtilities.RefreshHandLayout();
            }

        });
    }

    public void ExitUltimateMode() {
        PCLActions.Bottom.Callback(() -> {
            if (PCLCombatStats.OnTryChangeUltimateState(AbstractDungeon.player, this, false)) {
                inUltimateMode = false;
                Resolve = 0;
                PCLGameUtilities.RefreshHandLayout();
            }
        });
    }

    public void AddQueueItem() {
        AddQueueItem(Next.size());
    }

    protected void AddQueueItem(int i) {
        if (Next.size() < MAX_PREVIEWS) {
            Next.add(new AffinityKeywordButton(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, hb.width + (i + 2) * ICON_SIZE, -ICON_SIZE * 1.7f, false)
                    ,PICKS.Retrieve(PCLGameUtilities.GetRNG(), false))
                    .SetLevel(1)
                    .ShowBorders(i == 0)
                    .SetOnClick(__ -> {
                        if (Reroll != null) {
                            Reroll.OnClick();
                        }
                    }));
        }
    }

    public PCLAffinity AdvanceAffinities(int times) {
        if (Next.size() == 0) {
            return PCLAffinity.General;
        }
        for (int j = 0; j < times; j++) {
            for (int i = 0; i <= Next.size() - 2; i++) {
                Next.get(i).SetAffinity(Next.get(i+1).Type);
            }
            Next.get(Next.size() - 1).SetAffinity(PICKS.Retrieve(PCLGameUtilities.GetRNG(), false));
        }
        return Next.get(0).Type;
    }

    public int AddResolve(int addition) {
        Resolve = Mathf.Clamp(Resolve + addition, 0, MAX_RESOLVE);
        if (Resolve <= 0) {
            ExitUltimateMode();
        }
        return Resolve;
    }

    public int Resolve() {
        return Resolve;
    }

    public PCLAffinity GetCurrentAffinity() {
        return Next.size() == 0 ? PCLAffinity.General : Next.get(0).Type;
    }

    public boolean InUltimateMode() {
        return inUltimateMode;
    }

    public boolean TrySpendResolve(int amount) {
        if (Resolve >= amount) {
            AddResolve(-amount);
            return true;
        }
        return false;
    }

    @Override
    public int OnTrySpendEnergy(AbstractCard card, AbstractPlayer p, int originalCost) {
        if (InUltimateMode() && card instanceof EternalCard) {
            return 0;
        }
        return originalCost;
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
        tooltipInfo.description = tooltipDescriptions.Current();
        tooltipInfo.title = tooltipTitles.Current();
        tooltipInfo.subText.SetText(PGR.PCL.Strings.Misc.PressKeyToCycle(PCLHotkeys.cycle.getKeyString()) + " (" + (tooltipDescriptions.GetIndex() + 1) + "/" + tooltipDescriptions.Count() + ")");
    }
}
