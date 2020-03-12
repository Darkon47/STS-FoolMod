package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class YoshinoHimekawa extends AnimatorCard {
    public static final EYBCardData DATA = Register(YoshinoHimekawa.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    static
    {
        DATA.AddPreview(new Zadkiel(), true);
    }

    public YoshinoHimekawa() {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }
}