package eatyourbeets.cards.animator.curse;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Curse_GriefSeed extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(1, EYBCardTarget.None)
            .SetSeries(CardSeries.MadokaMagica);

    public Curse_GriefSeed()
    {
        super(DATA, false);

        Initialize(0, 0, 1);

        SetAffinity_Dark(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.WaitRealtime(0.3f);
        GameActions.Bottom.Callback(() ->
        {
            RandomizedList<AbstractPower> powers = new RandomizedList<>();
            for (AbstractPower p : player.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                {
                    powers.Add(p);
                }
            }

            if (powers.Size() > 0)
            {
                GameActions.Top.RemovePower(player, powers.Retrieve(rng));
            }
        });
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        GameActions.Bottom.Callback(() ->
        {
            GameActions.Bottom.Flash(this);
            GameActions.Bottom.DealDamage(null, player, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }
}
