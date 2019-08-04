package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.TemporaryEnvenomPower;

public class AcuraAkari extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraAkari.class.getSimpleName());

    public AcuraAkari()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GetOtherCardsInHand().size() > 0)
        {
            GameActionsHelper.Discard(1, false);

            for (int i = 0; i < secondaryValue; i++)
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
            }
        }

        GameActionsHelper.ApplyPower(p, p, new TemporaryEnvenomPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}