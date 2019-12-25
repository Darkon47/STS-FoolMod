package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.utilities.GameActions;

public class Greed extends AnimatorCard implements OnAddedToDeckSubscriber
{
    public static final String ID = Register(Greed.class.getSimpleName(), EYBCardBadge.Special);

    public Greed()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 2, 2, 30);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void OnAddedToDeck()
    {
        AbstractDungeon.player.gainGold(secondaryValue);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int discount = Math.floorDiv(AbstractDungeon.player.gold, 100);
        if (this.costForTurn > 0 && !this.freeToPlayOnce)
        {
            this.modifyCostForTurn(-discount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainPlatedArmor(magicNumber);
        GameActions.Bottom.GainMetallicize(magicNumber);
        GameActions.Bottom.StackPower(new MalleablePower(p, magicNumber));
    }
}