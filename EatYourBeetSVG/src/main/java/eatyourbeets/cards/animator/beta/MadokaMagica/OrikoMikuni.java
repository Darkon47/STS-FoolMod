package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrikoMikuni extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrikoMikuni.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Scry(magicNumber);

        if (!p.hand.isEmpty()) {
            GameActions.Bottom.DiscardFromHand(name, 1, false)
            .SetOptions(false, false, false);
        }

        if (!p.hand.isEmpty())
        {
            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .SetMessage(RetainCardsAction.TEXT[0])
            .SetFilter(c -> !c.isEthereal)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    AbstractCard card = cards.get(0);

                    GameUtilities.Retain(card);
                }
            });
        }
    }
}