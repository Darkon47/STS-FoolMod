package pinacolada.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.effects.player.ObtainRelicEffect;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.effects.player.SpawnRelicEffect;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.CallbackEffect2;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GameEffects;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;

import java.util.ArrayList;

public final class PCLGameEffects
{
    public final static ArrayList<AbstractGameEffect> UnlistedEffects = new ArrayList<>();
    public final static PCLGameEffects List = new PCLGameEffects(EffectType.List, GameEffects.List);
    public final static PCLGameEffects Queue = new PCLGameEffects(EffectType.Queue, GameEffects.Queue);
    public final static PCLGameEffects TopLevelList = new PCLGameEffects(EffectType.TopLevelList, GameEffects.TopLevelList);
    public final static PCLGameEffects TopLevelQueue = new PCLGameEffects(EffectType.TopLevelQueue, GameEffects.TopLevelQueue);
    public final static PCLGameEffects Manual = new PCLGameEffects(EffectType.Manual, GameEffects.Manual);

    protected final EffectType effectType;
    protected final GameEffects baseEffects;

    protected PCLGameEffects(EffectType effectType, GameEffects baseEffects)
    {
        this.effectType = effectType;
        this.baseEffects = baseEffects;
    }

    public ArrayList<AbstractGameEffect> GetList()
    {
        return baseEffects.GetList();
    }

    public static boolean IsEmpty()
    {
        for (AbstractGameEffect effect : AbstractDungeon.topLevelEffects)
        {
            if (effect instanceof EYBEffect)
            {
                return false;
            }
        }

        return UnlistedEffects.isEmpty();
    }

    public EYBEffect Attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax)
    {
        return Attack(source, target, attackEffect, pitchMin, pitchMax, null, source == target ? 0 : 0.15f);
    }

    public EYBEffect Attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax, Color vfxColor)
    {
        return Attack(source, target, attackEffect, pitchMin, pitchMax, vfxColor, source == target ? 0 : 0.15f);
    }

    public EYBEffect Attack(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, float pitchMin, float pitchMax, Color vfxColor, float spread)
    {
        AttackEffects.PlaySound(attackEffect, pitchMin, pitchMax);
        return AttackWithoutSound(source, target, attackEffect, vfxColor, spread);
    }

    public EYBEffect AttackWithoutSound(AbstractCreature source, AbstractCreature target, AbstractGameAction.AttackEffect attackEffect, Color vfxColor, float spread)
    {
        final EYBEffect effect = Add(AttackEffects.GetVFX(attackEffect, source, VFX.RandomX(target.hb, spread), VFX.RandomY(target.hb, spread)));
        if (vfxColor != null)
        {
            effect.SetColor(vfxColor);
        }

        return effect;
    }

    public int Count()
    {
        return GetList().size();
    }

    public <T extends AbstractGameEffect> T Add(T effect)
    {
        GetList().add(effect);

        return effect;
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect)
    {
        return Add(new CallbackEffect2(effect));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, ActionT0 onCompletion)
    {
        return Add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, ActionT1<AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, Object state, ActionT2<Object, AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, state, onCompletion));
    }

    public CallbackEffect Callback(ActionT0 onCompletion)
    {
        return Add(new CallbackEffect(new WaitAction(0.01f), onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action)
    {
        return Add(new CallbackEffect(action));
    }

    public CallbackEffect Callback(AbstractGameAction effect, ActionT0 onCompletion)
    {
        return Add(new CallbackEffect(effect, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action, Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, state, onCompletion));
    }

    public BorderFlashEffect BorderFlash(Color color)
    {
        return Add(new BorderFlashEffect(color, true));
    }

    public BorderLongFlashEffect BorderLongFlash(Color color)
    {
        return Add(new BorderLongFlashEffect(color, true));
    }

    public SpawnRelicEffect SpawnRelic(AbstractRelic relic, float x, float y)
    {
        return Add(new SpawnRelicEffect(relic, x, y));
    }

    public ObtainRelicEffect ObtainRelic(AbstractRelic relic)
    {
        return Add(new ObtainRelicEffect(relic));
    }

    public RemoveRelicEffect RemoveRelic(AbstractRelic relic)
    {
        return Add(new RemoveRelicEffect(relic));
    }

    public RoomTintEffect RoomTint(Color color, float transparency)
    {
        return Add(new RoomTintEffect(color.cpy(), transparency));
    }

    public ShowCardBrieflyEffect ShowCardBriefly(AbstractCard card)
    {
        return Add(new ShowCardBrieflyEffect(card));
    }

    public ShowCardBrieflyEffect ShowCardBriefly(AbstractCard card, float x, float y)
    {
        return Add(new ShowCardBrieflyEffect(card, x, y));
    }

    public ShowCardBrieflyEffect ShowCopy(AbstractCard card)
    {
        return ShowCardBriefly(card.makeStatEquivalentCopy());
    }

    public ShowCardAndObtainEffect ShowAndObtain(AbstractCard card)
    {
        return ShowAndObtain(card, Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f, true);
    }

    public ShowCardAndObtainEffect ShowAndObtain(AbstractCard card, float x, float y, boolean converge)
    {
        return Add(new ShowCardAndObtainEffect(card, x, y, converge));
    }

    public TalkEffect Talk(AbstractCreature source, String message)
    {
        return Add(new TalkEffect(source, message));
    }

    public TalkEffect Talk(AbstractCreature source, String message, float duration)
    {
        return Add(new TalkEffect(source, message, duration));
    }

    public CallbackEffect WaitRealtime(float duration)
    {
        return Add(new CallbackEffect(new WaitRealtimeAction(duration)));
    }

    public enum EffectType
    {
        List,
        Queue,
        TopLevelList,
        TopLevelQueue,
        Manual
    }
}