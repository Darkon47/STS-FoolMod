package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Special;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.monsters.PCLAlly;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.PGR;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLActions;

import java.util.UUID;

public class ChangeIntentPower extends PCLClickablePower
{
    public static final float BUTTON_SIZE = 64f;

    public final UUID uuid;
    public ActionT1<AbstractCreature> action;
    public ActionT1<ChangeIntentPower> powerUpdate;
    public String actionString;
    public EYBMove_Special specialMove;
    public boolean active;
    public static final String POWER_ID = PGR.PCL.CreateID(ChangeIntentPower.class.getSimpleName());
    public RelativeHitbox rhb;

    public ChangeIntentPower(PCLAlly owner, AbstractMonster.Intent intent, ActionT1<AbstractCreature> action) {
        this(owner, intent, action, null, null);
    }

    public ChangeIntentPower(PCLAlly owner, AbstractMonster.Intent intent, ActionT1<AbstractCreature> action, ActionT1<ChangeIntentPower> powerUpdate) {
        this(owner, intent, action, powerUpdate, null);
    }

    public ChangeIntentPower(PCLAlly owner, AbstractMonster.Intent intent, ActionT1<AbstractCreature> action, ActionT1<ChangeIntentPower> powerUpdate, Texture powerImg)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Affinity, 0);
        this.uuid = owner.uuid;
        this.triggerCondition.SetUses(-1, false, false);
        this.action = action;
        this.powerUpdate = powerUpdate;
        this.hideAmount = true;
        this.img = powerImg != null ? powerImg : PGR.PCL.Images.Affinities.General.Texture();

        this.specialMove = (EYBMove_Special) new EYBMove_Special()
                .SetIntent(intent)
                .SkipNormalAction(true)
                .SetOnUse((m, c) -> {
                    PCLActions.Bottom.Callback(m, (t1, __) -> {
                        action.Invoke(c);
                    });
            });

        final float size = BUTTON_SIZE * Settings.scale * 1.5f;
        hb = rhb = new RelativeHitbox(owner.hb, size, size);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (powerUpdate != null) {
            powerUpdate.Invoke(this);
        }
        rhb.SetOffset(-hb.width, slot * hb.height * 1.1f).update();
    }

    @Override
    public String GetUpdatedDescription()
    {
        PCLAffinity first = triggerCondition.affinities.length > 0 ? triggerCondition.affinities[0] : PCLAffinity.General;

        return FormatDescription(0
                , triggerCondition.requiredAmount
                , first.GetTooltip()
                , actionString
                , active ? powerStrings.DESCRIPTIONS[1] : !triggerCondition.checkCondition.Invoke(triggerCondition.requiredAmount) ? powerStrings.DESCRIPTIONS[2] : "");
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        Color borderColor = (enabled && triggerCondition.CanUse()) ? c : disabledColor;
        Color imageColor = enabled ? c : disabledColor;

        if (hb.cX != x || hb.cY != y)
        {
            hb.move(x, y);
        }

        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, borderColor, PGR.PCL.Images.SquaredButton.Texture(), x, y, BUTTON_SIZE, BUTTON_SIZE, 1.5f, 0);
        if (this.powerIcon != null) {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, imageColor, this.powerIcon, x, y, BUTTON_SIZE, BUTTON_SIZE, 0.75f, 0);
        }
        else {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, imageColor, this.img, x, y, BUTTON_SIZE, BUTTON_SIZE, 0.75f, 0);
        }

        if (enabled && hb.hovered && clickable)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Colors.White(0.3f), PGR.PCL.Images.SquaredButton.Texture(), x, y, BUTTON_SIZE, BUTTON_SIZE, 1.5f, 0);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    @Override
    public void OnClick() {
        if (CanSelect())
        {
            CombatStats.TryActivateSemiLimited(uuid.toString());
            specialMove.Select(true);
            active = true;
            updateDescription();
        }
    }

    public ChangeIntentPower AddToMoveset(EYBMoveset moveset) {
        moveset.Special.Add(specialMove);
        return this;
    }

    public void Select(boolean refreshIntent) {
        this.specialMove.Select(refreshIntent);
    }

    public boolean CanSelect() {
        return !active && CombatStats.CanActivateSemiLimited(uuid.toString()) && triggerCondition.CanUse();
    }

}