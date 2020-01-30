package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.attack.LaserBeam2Effect;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.interfaces.subscribers.OnAfterCardExhaustedSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NivaLada extends AnimatorCard_UltraRare implements OnBattleStartSubscriber, OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber
{
    public static final String ID = Register(NivaLada.class);

    public NivaLada()
    {
        super(ID, 0, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 300);
        SetUpgrade(0, 0, 0);

        if (GameUtilities.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetCooldown(18, -2, this::OnCooldownCompleted);
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardDiscarded.Subscribe(this);
        PlayerStatistics.onAfterCardExhausted.Subscribe(this);
    }

    @Override
    public void OnAfterCardExhausted(AbstractCard card)
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown();
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (this.secondaryValue > 0)
        {
            cooldown.ProgressCooldown();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = GameUtilities.GetRandomEnemy(true);
        }

        if (m.hasPower(IntangiblePower.POWER_ID))
        {
            GameActions.Bottom.RemovePower(m, m, IntangiblePower.POWER_ID);
        }

        GameActions.Bottom.VFX(new LaserBeam2Effect(player.hb.cX, player.hb.cY), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F);
        GameActions.Bottom.DealDamage(player, m, this.magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }
}