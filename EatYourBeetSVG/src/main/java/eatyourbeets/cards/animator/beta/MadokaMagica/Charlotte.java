package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class Charlotte extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Charlotte.class).SetAttack(4, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Charlotte()
    {
        super(DATA);

        Initialize(8, 0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage * (float) Math.pow(2, player.hand.getCardsOfType(CardType.CURSE).size());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        // This would need a flashier VFX when the damage is above 64 or something
        GameActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE);
    }
}