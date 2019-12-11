package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.RefreshHandLayoutAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class HiiragiShinya extends AnimatorCard
{
    public static final String ID = Register(HiiragiShinya.class.getSimpleName(), EYBCardBadge.Synergy);

    public HiiragiShinya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,4, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetMessage(MoveCardsAction.TEXT[0])
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard c = cards.get(0);
                c.setCostForTurn(c.costForTurn + 1);
                c.retain = true;
                GameActions.Bottom.Add(new RefreshHandLayoutAction());
            }
        });

        if (HasActiveSynergy())
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}