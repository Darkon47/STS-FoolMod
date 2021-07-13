package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Millim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Millim.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental);

    public Millim()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(1, 0, 0);
        SetScaling(1, 1, 1);

        SetUnique(true, true);
        SetSeries(CardSeries.TenSura);
        SetAffinity(2, 0, 2, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 2 == 1)
        {
            upgradeMagicNumber(1);
        }
        else
        {
            upgradeDamage(2);
        }

        this.upgradedMagicNumber = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (isSynergizing)
        {
            GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 2));
        }
    }
}