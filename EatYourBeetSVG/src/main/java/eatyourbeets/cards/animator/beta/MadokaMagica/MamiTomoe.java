package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class MamiTomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamiTomoe.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);

    public MamiTomoe()
    {
        super(DATA);

        // For attacks, damage should be used instead of magicNumber,
        // otherwise it will not scale with Vulnerable, Weak, and similar effects
        Initialize(12, 0);
        SetUpgrade(3, 0);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = player.discardPile.getCardsOfType(CardType.CURSE).size();
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb_x, m.hb_y));

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}
