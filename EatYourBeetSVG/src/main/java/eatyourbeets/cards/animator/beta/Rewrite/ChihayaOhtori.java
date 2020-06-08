package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChihayaOhtori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChihayaOhtori.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);

    public ChihayaOhtori()
    {
        super(DATA);

        Initialize(8, 0, 1, 1);
        SetUpgrade(3, 0, 0);
        SetScaling(1, 0, 1);

        SetSynergy(Synergies.Rewrite);
        SetHaste(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (hasTag(HASTE))
        {
            GameActions.Bottom.GainTemporaryArtifact(secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);

        GameActions.Top.FetchFromPile(name, 1, player.discardPile)
        .SetOptions(false, false)
        .SetFilter(c -> c.hasTag(MARTIAL_ARTIST))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameUtilities.ModifyCostForTurn(cards.get(0), -1, true);
                GameActions.Bottom.Add(new RefreshHandLayout());
            }
        });
    }
}