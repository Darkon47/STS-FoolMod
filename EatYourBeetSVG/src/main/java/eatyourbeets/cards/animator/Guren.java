package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.GurenAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Guren extends AnimatorCard
{
    public static final String ID = CreateFullID(Guren.class.getSimpleName());

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0, 0,3);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new GurenAction(null, this.magicNumber));
        GameActionsHelper.AddToBottom(new GurenAction(null, this.magicNumber));
        GameActionsHelper.AddToBottom(new GurenAction(null, this.magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}