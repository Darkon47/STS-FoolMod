package eatyourbeets.cards.animator.beta;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAddedToDeckSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardPoolChangedSubscriber;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Lu extends AnimatorCard implements OnAddedToDeckSubscriber, OnCardPoolChangedSubscriber
{
    public static final EYBCardData DATA = Register(Lu.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);

    public Lu()
    {
        super(DATA);

        Initialize(2, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(2, 0, 2);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void OnAddedToDeck()
    {
        OnCardPoolChanged();
    }

    @Override
    public void OnCardPoolChanged()
    {
        AbstractDungeon.uncommonCardPool.removeCard(cardID);
        AbstractDungeon.srcUncommonCardPool.removeCard(cardID);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAttackTarget(EYBCardTarget.ALL);
        SetMultiDamage(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Add(new EvokeAllOrbsAction());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Frost(), true);
        GameActions.Bottom.ChannelOrb(new Dark(), true);
        if (isMultiDamage)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect((enemy, __) -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)));
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)));
        }

        if (damage > 20)
        {
            GameActions.Bottom.Add(new ShakeScreenAction(0.8f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        }
    }
}