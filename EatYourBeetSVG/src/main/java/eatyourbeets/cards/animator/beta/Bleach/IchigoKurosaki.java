package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class IchigoKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random);
    static
    {
        DATA.AddPreview(new IchigoBankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(2, 0, 0, ForcePower.GetThreshold(4));
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0,1,1);

        SetSynergy(Synergies.Bleach);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);

        if (ForcePower.GetCurrentLevel() > 4)
        {
            GameActions.Bottom.MakeCardInDrawPile(new IchigoBankai());
            GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
        }
    }
}