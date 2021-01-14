package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Soujiro extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new Soujiro_Isami(), false);
        DATA.AddPreview(new Soujiro_Kawara(), false);
        DATA.AddPreview(new Soujiro_Kyouko(), false);
    }

    public Soujiro()
    {
        super(DATA);

        Initialize(12, 0, 5);
        SetUpgrade(3, 0, 2);
        SetScaling(0,1, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();

        int synergyCount = 0;

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            if (super.HasSynergy(c))
            {
                synergyCount++;
            }
        }

        damage += synergyCount * magicNumber;

        return damage;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && CombatStats.TryActivateLimited(cardID))
        {
            GameEffects.List.ShowCopy(this);

            final float speed = Settings.ACTION_DUR_XFAST;

            GameActions.Top.MakeCard(new Soujiro_Isami(), player.drawPile)
                    .SetDuration(speed, true);
            GameActions.Top.MakeCard(new Soujiro_Kawara(), player.drawPile)
                    .SetDuration(speed, true);
            GameActions.Top.MakeCard(new Soujiro_Kyouko(), player.drawPile)
                    .SetDuration(speed, true);
        }
    }
}