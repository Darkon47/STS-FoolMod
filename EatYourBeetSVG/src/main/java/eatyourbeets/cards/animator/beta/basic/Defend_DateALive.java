package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_DateALive extends Defend
{
    public static final String ID = Register(Defend_DateALive.class).ID;

    public Defend_DateALive()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6, 2, 30);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        int totalCards = player.drawPile.size() + player.discardPile.size() + player.hand.size();
        if (totalCards >= secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(magicNumber);
        }
    }
}