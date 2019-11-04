package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.actions.common.OnDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Berserker extends AnimatorCard
{
    public static final String ID = Register(Berserker.class.getSimpleName(), EYBCardBadge.Special);

    public Berserker()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(28,14, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY);

            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, m.currentBlock, true));
            GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

            if (p.currentHealth / (float)p.maxHealth < 0.1f && PlayerStatistics.TryActivateLimited(cardID))
            {
                AbstractCard tmp = makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = current_x;
                tmp.current_y = current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;

                if (tmp.cost > 0)
                {
                    tmp.freeToPlayOnce = true;
                }

                tmp.calculateCardDamage(m);
                tmp.purgeOnUse = true;

                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, energyOnUse, true));
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(4);
            upgradeBlock(4);
        }
    }

    private void OnDamage(Object state, AbstractMonster monster)
    {
        Integer initialBlock = Utilities.SafeCast(state, Integer.class);
        if (initialBlock == null || monster == null)
        {
            return;
        }

        if (monster.isDeadOrEscaped() || (initialBlock > 0 && monster.currentBlock <= 0))
        {
            GameActionsHelper.GainBlock(AbstractDungeon.player, this.block);
        }
    }
}