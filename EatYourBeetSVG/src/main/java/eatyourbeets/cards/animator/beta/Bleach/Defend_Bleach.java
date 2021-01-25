package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Defend_Bleach extends Defend
{
    public static final String ID = Register(Defend_Bleach.class).ID;

    public Defend_Bleach()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 3);
        SetCooldown(1, 0, this::OnCooldownCompleted);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .ShowEffect(false, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            for (int i = 0; i < secondaryValue; i++)
            {
                GameActions.Bottom.ChannelOrb(new Fire(), true);
            }
        });
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {

    }
}