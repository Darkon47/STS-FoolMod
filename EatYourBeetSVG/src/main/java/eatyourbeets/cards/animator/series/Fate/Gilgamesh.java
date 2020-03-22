package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import patches.StoreRelicPatches;

public class Gilgamesh extends AnimatorCard implements OnRelicObtainedSubscriber
{
    private static AbstractRelic lastRelicObtained = null;

    public static final EYBCardData DATA = Register(Gilgamesh.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged);
    public static final int GOLD_REWARD = 25;

    public Gilgamesh()
    {
        super(DATA);

        Initialize(3, 0, 3, GOLD_REWARD);
        SetUpgrade(1, 0);

        SetUnique(true, true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (lastRelicObtained == relic)
        {
            return;
        }

        lastRelicObtained = relic;

        if (!(relic instanceof UnnamedReignRelic))
        {
            for (AbstractRelic r : StoreRelicPatches.last20Relics)
            {
                if (r == relic)
                {
                    return;
                }
            }

            final float pos_x = (float) Settings.WIDTH / 4f;
            final float pos_y = (float) Settings.HEIGHT / 2f;

            upgrade();

            player.bottledCardUpgradeCheck(this);
            player.gainGold(GOLD_REWARD);

            if (GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(pos_x, pos_y));
                GameEffects.TopLevelQueue.ShowCardBriefly(makeStatEquivalentCopy(), pos_x, pos_y);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (timesUpgraded >= 8)
        {
            GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));
            GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.1f);
            GameActions.Bottom.SFX("ATTACK_WHIRLWIND");
            GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

            for (int i = 0; i < this.magicNumber; i++)
            {
                GameActions.Bottom.SFX("ATTACK_HEAVY");
                GameActions.Bottom.VFX(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX), 0.1f);
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.VFX(new CleaveEffect());
            }
        }
        else
        {
            for (int i = 0; i < this.magicNumber; i++)
            {
                GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            }
        }
    }
}