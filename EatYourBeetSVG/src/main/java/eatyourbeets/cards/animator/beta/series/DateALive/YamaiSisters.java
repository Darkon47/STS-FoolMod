package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class YamaiSisters extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YamaiSisters.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Normal);

    public YamaiSisters()
    {
        super(DATA);

        Initialize(2, 0, 8);
        SetUpgrade(1, 0);

        SetUnique(false, true);
        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded == magicNumber)
        {
            SetScaling(1, 1, 1);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ModifyAllCopies(cardID, card ->
        {
            if (card.canUpgrade())
            {
                card.upgrade();
                card.flash();
            }
        });
    }

    @Override
    public boolean canUpgrade()
    {
        return timesUpgraded < magicNumber;
    }
}