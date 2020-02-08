package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.attack.Hemokinesis2Effect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Shalltear()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(1, 0, 1);
        SetScaling(1, 1, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect((enemy, aBoolean) ->
        {
            GameEffects.List.Add(new Hemokinesis2Effect(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
            GameActions.Bottom.ApplyWeak(player, enemy, 1);

            if (HasSynergy())
            {
                GameActions.Bottom.ReduceStrength(enemy, 1, true).SetForceGain(true);
            }
        });
    }
}