package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class LizardPriest extends AnimatorCard
{
    public static final String ID = Register(LizardPriest.class, EYBCardBadge.Synergy);

    public LizardPriest()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 8, 1, 2);
        SetUpgrade(0, 3, 0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActions.Bottom.Add(new RemoveAllBlockAction(m, p));
            GameActions.Bottom.Add(new GainBlockAction(m, p, this.magicNumber, true));
        }

        GameActions.Bottom.GainBlock(this.block);

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new TemporaryRetainPower(p, secondaryValue));
        }
    }
}