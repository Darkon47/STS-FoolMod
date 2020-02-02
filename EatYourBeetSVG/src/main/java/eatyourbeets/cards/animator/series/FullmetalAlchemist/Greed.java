package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Greed extends AnimatorCard
{
    public static final String ID = Register(Greed.class);

    public Greed()
    {
        super(ID, 2, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2, 150);
        SetUpgrade(0, 4, 0, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainPlatedArmor(magicNumber);
        GameActions.Bottom.GainMetallicize(magicNumber);
        GameActions.Bottom.StackPower(new MalleablePower(p, magicNumber));

        if (EffectHistory.TryActivateLimited(cardID))
        {
            int energy = Math.floorDiv(AbstractDungeon.player.gold, secondaryValue);
            if (energy > 0)
            {
                GameActions.Bottom.StackPower(new EnergizedBluePower(p, energy));
            }
        }
    }
}