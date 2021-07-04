package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.SheerColdPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class SheerCold extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SheerCold.class).SetPower(2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public SheerCold()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (JUtils.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID)) == 0)
            GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.StackPower(new SheerColdPower(p, magicNumber));
    }
}