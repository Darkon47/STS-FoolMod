package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.PhantasmalPower;
import eatyourbeets.actions.common.ModifyCostAction;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.RetainCardsOfTypeAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final String ID = CreateFullID(TanyaDegurechaff_Type95.class.getSimpleName());

    public TanyaDegurechaff_Type95()
    {
        super(ID, 4, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        Initialize(0, 0);

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (cost > 0)
        {
            GameActionsHelper.AddToBottom(new ReduceCostAction(this.uuid, 1));
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (cost > 0)
        {
            GameActionsHelper.AddToBottom(new ReduceCostAction(this.uuid, 1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Plasma(), true);
        GameActionsHelper.AddToBottom(new ModifyCostAction(this.uuid, 4));
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}