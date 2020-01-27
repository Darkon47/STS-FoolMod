package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;

public class Albedo extends AnimatorCard
{
    public static final String ID = Register(Albedo.class, EYBCardBadge.Synergy);

    public Albedo()
    {
        super(ID, 2, CardRarity.RARE, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, damage));

        if (HasSynergy())
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
        }
    }
}