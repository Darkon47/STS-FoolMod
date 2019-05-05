package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.LaserBeam2Effect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.subscribers.OnAfterCardExhaustedSubscriber;
import eatyourbeets.subscribers.OnBattleStartSubscriber;

public class NivaLada extends AnimatorCard_UltraRare implements OnBattleStartSubscriber, OnAfterCardExhaustedSubscriber, OnAfterCardDiscardedSubscriber
{
    public static final String ID = CreateFullID(NivaLada.class.getSimpleName());

    public NivaLada()
    {
        super(ID, 1, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 300);

        this.baseSecondaryValue = this.secondaryValue = GetBaseCooldown();

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

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
            ProgressCooldown();
        }
    }

    @Override
    public void OnAfterCardDiscarded()
    {
        if (this.secondaryValue > 0)
        {
            ProgressCooldown();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (ProgressCooldown())
        {
            OnCooldownCompleted(AbstractDungeon.player, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true));
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        this.isSecondaryValueModified = (this.secondaryValue == 0);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(-2);
        }
    }

    protected int GetBaseCooldown()
    {
        return upgraded ? 14 : 16;
    }

    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        if (m == null || m.isDeadOrEscaped())
        {
            m = PlayerStatistics.GetRandomEnemy(true);
        }

        if (m.hasPower(IntangiblePower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(m, m, IntangiblePower.POWER_ID));
        }

        GameActionsHelper.AddToBottom(new VFXAction(new LaserBeam2Effect(p.hb.cX, p.hb.cY), 0.1F));
        GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F));
        GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F));
        GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX + MathUtils.random(-0.05F, 0.05F), m.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F));
        GameActionsHelper.DamageTarget(p, m, this.magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }

    protected boolean ProgressCooldown()
    {
        boolean activate;
        int newValue;
        if (secondaryValue <= 0)
        {
            newValue = GetBaseCooldown();
            activate = true;
        }
        else
        {
            newValue = secondaryValue - 1;
            activate = false;
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            AnimatorCard_Cooldown card = (AnimatorCard_Cooldown)c;
            card.baseSecondaryValue = card.secondaryValue = newValue;
            //card.applyPowers();
        }

        this.baseSecondaryValue = this.secondaryValue = newValue;

        return activate;
    }
}