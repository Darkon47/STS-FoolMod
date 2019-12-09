package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Ara extends AnimatorCard implements MartialArtist
{
    public static final EYBCardBadge[] BADGES = {EYBCardBadge.Discard};
    public static final String ID = Register(Ara.class.getSimpleName(), EYBCardBadge.Special);

    public Ara()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(3,0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + MartialArtist.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActions.Bottom.DealDamage(this, m,  AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(GameUtilities.GetDebuffsCount(m.powers));
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards.get(0).type.equals(CardType.POWER) && EffectHistory.TryActivateSemiLimited(cardID))
            {
                GameActions.Bottom.GainAgility(1);
                GameActions.Bottom.GainForce(2);
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}