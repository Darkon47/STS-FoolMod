package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuiTsuruno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(9, 0, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        if (AbstractDungeon.cardRandomRng.randomBoolean(0.5f))
        {
            GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());
        }
        else if (upgraded)
        {
            GameActions.Bottom.ChannelOrb(new Fire(), true);
        }
    }
}