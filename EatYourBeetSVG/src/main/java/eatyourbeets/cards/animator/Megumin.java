package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class Megumin extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Megumin.class.getSimpleName(), EYBCardBadge.Synergy);

    public Megumin()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(12, 0);

        SetMultiDamage(true);
        SetExhaust(true);
        SetUnique(true);
        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + (Spellcaster.GetScaling() * 4));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.1F);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.2F);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.ORANGE));
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE", 0.3F);
        GameActions.Bottom.Wait(0.35f);
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);

        for (AbstractCreature m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.VFX(new FlameBarrierEffect(m1.hb_x, m1.hb_y));
            GameActions.Bottom.VFX(new ExplosionSmallEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);

        if (HasActiveSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            for (AbstractCard c : GameUtilities.GetAllInstances(this))
            {
                c.upgrade();
            }
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;

        upgradeDamage(timesUpgraded % 2 == 0 ? 3 : 2);

        this.upgraded = true;
        this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}