package eatyourbeets.cards.animator.beta.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.MakeTempCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.attack.SmallLaser2Effect;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, Spellcaster, OnBattleStartSubscriber, OnShuffleSubscriber, OnAddedToDrawPileSubscriber
{
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    public MukuroHoshimiya()
    {
        super(DATA);

        Initialize(6, 0);

        SetSynergy(Synergies.DateALive);

        if (CanSubscribeToEvents())
        {
            OnBattleStart();
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetScaling(2, 2, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + player.drawPile.size();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy -> GameEffects.Queue.Add(new SmallLaser2Effect(player.hb.cX, player.hb.cY,
        enemy.hb.cX + MathUtils.random(-0.05F, 0.05F), enemy.hb.cY + MathUtils.random(-0.05F, 0.05F), Color.PURPLE)));
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        OnShuffle(false);

        return false;
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onShuffle.Subscribe(this);
    }

    @Override
    public void OnAddedToDrawPile(boolean visualOnly, MakeTempCard.Destination destination)
    {
        OnShuffle(false);
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        GameActions.Top.Callback(__ ->
        {
            CardGroup group = player.drawPile;
            if (group.group.remove(this))
            {
                group.group.add(group.size() - Math.min(group.size(), 5), this);
            }
        });
    }
}