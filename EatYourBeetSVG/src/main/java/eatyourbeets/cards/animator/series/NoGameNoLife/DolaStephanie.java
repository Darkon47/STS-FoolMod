package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class DolaStephanie extends AnimatorCard
{
    public static final String ID = Register(DolaStephanie.class.getSimpleName());

    public DolaStephanie()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .SetMessage(AnimatorResources_Strings.HandSelection.TEXT[1])
        .SetFilter(c -> c instanceof AnimatorCard)
        .AddCallback(cards ->
        {
            AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
            if (card != null)
            {
                GameActions.Top.FetchFromPile(name, 1, AbstractDungeon.player.drawPile)
                .SetOptions(false, false)
                .SetFilter(card::HasSynergy);
            }
        });
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }
}