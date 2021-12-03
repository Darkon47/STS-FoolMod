package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.EYBCardAffinityRow;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Objects;

public abstract class EYBStance extends AbstractStance
{
    protected static final HashMap<String, FuncT0<EYBStance>> stances = new HashMap<>();
    protected static final HashMap<String, EYBCardTooltip> tooltips = new HashMap<>();
    protected static long sfxId = -1L;
    protected final AbstractCreature owner;
    protected final StanceStrings strings;
    public final Affinity affinity;

    public static void Initialize()
    {
        stances.clear();
        stances.put(MightStance.STANCE_ID, MightStance::new);
        stances.put(WisdomStance.STANCE_ID, WisdomStance::new);
        stances.put(VelocityStance.STANCE_ID, VelocityStance::new);
        stances.put(EnduranceStance.STANCE_ID, EnduranceStance::new);
        stances.put(SuperchargeStance.STANCE_ID, SuperchargeStance::new);
        stances.put(DesecrationStance.STANCE_ID, DesecrationStance::new);

        tooltips.clear();
        tooltips.put(MightStance.STANCE_ID, GR.Tooltips.MightStance);
        tooltips.put(VelocityStance.STANCE_ID, GR.Tooltips.VelocityStance);
        tooltips.put(WisdomStance.STANCE_ID, GR.Tooltips.WisdomStance);
        tooltips.put(EnduranceStance.STANCE_ID, GR.Tooltips.EnduranceStance);
        tooltips.put(SuperchargeStance.STANCE_ID, GR.Tooltips.SuperchargeStance);
        tooltips.put(DesecrationStance.STANCE_ID, GR.Tooltips.DesecrationStance);
        tooltips.put(NeutralStance.STANCE_ID, GR.Tooltips.NeutralStance);
    }

    public static EYBCardTooltip GetStanceTooltip(String stance)
    {
        return tooltips.getOrDefault(stance, null);
    }

    public static AbstractStance GetRandomStance() {
        FuncT0<EYBStance> constructor = JUtils.Random(stances.values());
        if (constructor == null) {
            return new NeutralStance();
        }
        return constructor.Invoke();
    }

    public static EYBStance GetStanceFromName(String name)
    {
        return stances.containsKey(name) ? stances.get(name).Invoke() : null;
    }

    public static String CreateFullID(Class<? extends EYBStance> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    protected static Color CreateColor(float r1, float r2, float g1, float g2, float b1, float b2)
    {
        return new Color(MathUtils.random(r1, r2), MathUtils.random(g1, g2), MathUtils.random(b1, b2), 0);
    }

    protected abstract Color GetAuraColor();
    protected abstract Color GetParticleColor();
    protected abstract Color GetMainColor();

    protected EYBStance(String id, Affinity affinity, AbstractCreature owner)
    {
        this.ID = id;
        this.strings = GR.GetStanceString(id);
        this.name = strings.NAME;
        this.owner = owner;
        this.affinity = affinity;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = JUtils.Format(strings.DESCRIPTION[0], EYBCardAffinityRow.SYNERGY_MULTIPLIER);
    }

    @Override
    public void updateAnimation()
    {
        if (!Settings.DISABLE_EFFECTS)
        {
            this.particleTimer -= GR.UI.Delta();
            if (this.particleTimer < 0f)
            {
                this.particleTimer = 0.04f;
                QueueParticle();
            }
        }

        this.particleTimer2 -= GR.UI.Delta();
        if (this.particleTimer2 < 0f)
        {
            this.particleTimer2 = MathUtils.random(0.45f, 0.55f);
            QueueAura();
        }
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        if (sfxId != -1L)
        {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        GameEffects.Queue.Add(new BorderFlashEffect(GetMainColor(), true));

        if (TryApplyStance(ID))
        {
            CombatStats.Affinities.GetPower(affinity).SetScalingMultiplier(2);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        this.stopIdleSfx();

        if (TryApplyStance(null))
        {
            CombatStats.Affinities.GetPower(affinity).SetScalingMultiplier(1);
            GameActions.Bottom.AddAffinity(affinity, EYBCardAffinityRow.SYNERGY_MULTIPLIER);
        }
    }

    public void onRefreshStance()
    {
        GameActions.Bottom.AddAffinity(affinity, EYBCardAffinityRow.SYNERGY_MULTIPLIER);
    }

    @Override
    public void stopIdleSfx()
    {
        if (sfxId != -1L)
        {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }

    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleVertical(GetParticleColor()));
    }

    protected void QueueAura()
    {
        GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    protected String FormatDescription(Object... args)
    {
        return JUtils.Format(strings.DESCRIPTION[0], args);
    }

    protected boolean TryApplyStance(String stanceID)
    {
        String current = CombatStats.GetCombatData(EYBStance.class.getSimpleName(), null);
        if (Objects.equals(stanceID, current))
        {
            return false;
        }

        CombatStats.SetCombatData(EYBStance.class.getSimpleName(), stanceID);
        return true;
    }
}
